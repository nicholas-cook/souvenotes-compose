package com.souvenotes.repository.notes

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.souvenotes.repository.model.FirebaseNoteItem
import com.souvenotes.repository.model.FirebaseNotesListItem
import com.souvenotes.repository.model.NotesListItem
import com.souvenotes.repository.user.UserRepository
import com.souvenotes.repository.utils.DateTimeUtils
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(private val userRepository: UserRepository) :
    NotesRepository {

    private val databaseReference = FirebaseDatabase.getInstance().reference

    private var notesListQuery: Query? = null
    private var notesListValueEventListener: ValueEventListener? = null

    private var noteReference: DatabaseReference? = null
    private var noteValueEventListener: ValueEventListener? = null

    override fun addNotesListListener(notesListListener: NotesListListener) {
        val userId = userRepository.getUserId()
        if (userId != null) {
            notesListValueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!userRepository.isUserLoggedIn()) {
                        notesListListener.onNotesListError("User not logged in", true)
                        return
                    }
                    val listItems = mutableListOf<NotesListItem>()
                    snapshot.children.forEach {
                        it.getValue(FirebaseNotesListItem::class.java)?.let { fbNotesListItem ->
                            listItems.add(
                                NotesListItem(
                                    title = fbNotesListItem.title,
                                    dateTimeText = DateTimeUtils.getDateTimeText(-1 * fbNotesListItem.timestamp),
                                    key = it.key ?: "",
                                    createdAt = fbNotesListItem.createdAt
                                )
                            )
                        }
                    }
                    notesListListener.onNotesListAvailable(listItems)
                }

                override fun onCancelled(error: DatabaseError) {
                    notesListListener.onNotesListError(
                        error.message,
                        isAuthException(error)
                    )
                }

            }
            notesListValueEventListener?.let {
                notesListQuery =
                    databaseReference.child("notes-list").child(userId).orderByChild("timestamp")
                        .limitToLast(110).apply { addValueEventListener(it) }
            }
        } else {
            notesListListener.onNotesListError("User not logged in", true)
        }
    }

    override fun removeNotesListListener() {
        notesListValueEventListener?.let {
            notesListQuery?.removeEventListener(it)
        }
        notesListQuery = null
        notesListValueEventListener = null
    }

    override fun addNoteListener(noteKey: String, noteListener: NoteListener) {
        val userId = userRepository.getUserId()
        if (userId != null) {
            noteValueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!userRepository.isUserLoggedIn()) {
                        noteListener.onNoteError("User not logged in", true)
                        return
                    }
                    val note = snapshot.getValue(FirebaseNoteItem::class.java)
                    if (note != null) {
                        noteListener.onNoteAvailable(note.title, note.content)
                    } else {
                        noteListener.onNoteError("Unable to load note", false)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    noteListener.onNoteError(error.message, isAuthException(error))
                }
            }
            noteValueEventListener?.let {
                noteReference = databaseReference.child("notes").child(userId).child(noteKey)
                    .apply { addValueEventListener(it) }
            }
        } else {
            noteListener.onNoteError("User not logged in", true)
        }
    }

    override fun removeNoteListener() {
        noteValueEventListener?.let {
            noteReference?.removeEventListener(it)
        }
        noteReference = null
        noteValueEventListener = null
    }

    override fun deleteNote(noteKey: String, deleteListener: (success: Boolean) -> Unit) {
        val userId = userRepository.getUserId()
        if (userId != null) {
            val childUpdates = hashMapOf<String, Any?>()
            childUpdates["/notes/$userId/$noteKey"] = null
            childUpdates["/notes-list/$userId/$noteKey"] = null
            databaseReference.updateChildren(childUpdates).addOnCompleteListener { task ->
                deleteListener(task.isSuccessful)
            }
        }
    }

    override fun addNote(title: String, content: String): String {
        val userId = userRepository.getUserId()
        if (userId != null) {
            val notesValues = hashMapOf(Pair("title", title), Pair("content", content))
            val timestamp = System.currentTimeMillis()
            val notesListValues = hashMapOf(
                Pair("title", title),
                Pair("timestamp", -1 * timestamp),
                Pair("createdAt", timestamp)
            )

            val key = databaseReference.child("notes").push().key
            val childUpdates = hashMapOf<String, Any>()
            childUpdates["/notes/$userId/$key"] = notesValues
            childUpdates["/notes-list/$userId/$key"] = notesListValues

            databaseReference.updateChildren(childUpdates)
            return key ?: ""
        }
        return ""
    }

    override fun updateNote(title: String, content: String, createdAt: Long, noteKey: String) {
        val userId = userRepository.getUserId()
        if (userId != null) {
            val notesValues = hashMapOf(Pair("title", title), Pair("content", content))
            val timestamp = System.currentTimeMillis()
            val notesListValues = hashMapOf(
                Pair("title", title),
                Pair("timestamp", -1 * timestamp),
                Pair("createdAt", createdAt)
            )

            val childUpdates = hashMapOf<String, Any>()
            childUpdates["/notes/$userId/$noteKey"] = notesValues
            childUpdates["/notes-list/$userId/$noteKey"] = notesListValues

            databaseReference.updateChildren(childUpdates)
        }
    }

    override fun deleteAllUserNotes(onDeleteSuccess: () -> Unit, onDeleteError: () -> Unit) {
        val userId = userRepository.getUserId()
        if (userId != null) {
            val childUpdates = hashMapOf<String, Any?>(
                Pair("/notes/$userId", null),
                Pair("/notes-list/$userId", null)
            )
            databaseReference.updateChildren(childUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onDeleteSuccess()
                } else {
                    onDeleteError()
                }
            }
        } else {
            onDeleteError()
        }
    }

    private fun isAuthException(databaseError: DatabaseError): Boolean {
        return when (databaseError.code) {
            DatabaseError.PERMISSION_DENIED, DatabaseError.EXPIRED_TOKEN, DatabaseError.INVALID_TOKEN -> true
            else -> false
        }
    }
}