package com.souvenotes.souvenotes.repositories

import com.souvenotes.repository.notes.NoteListener
import com.souvenotes.repository.notes.NotesListListener
import com.souvenotes.repository.notes.NotesRepository
import com.souvenotes.repository.user.UserRepository
import com.souvenotes.souvenotes.NOTES_LIST
import com.souvenotes.souvenotes.NOTE_CONTENT
import com.souvenotes.souvenotes.NOTE_TITLE
import com.souvenotes.souvenotes.VALID_NOTE_KEY

class FakeNotesRepository(private val userRepository: UserRepository) : NotesRepository {

    var notesListError = false

    override fun addNotesListListener(notesListListener: NotesListListener) {
        if (!userRepository.isUserLoggedIn()) {
            notesListListener.onNotesListError("User not logged in", true)
        } else if (notesListError) {
            notesListListener.onNotesListError("Error retrieving notes", false)
        } else {
            notesListListener.onNotesListAvailable(NOTES_LIST)
        }
    }

    override fun removeNotesListListener() {
        // No-op
    }

    var editNoteError = false
    var currentNoteTitle = NOTE_TITLE
    var currentNoteContent = NOTE_CONTENT

    override fun addNoteListener(noteKey: String, noteListener: NoteListener) {
        if (!userRepository.isUserLoggedIn()) {
            noteListener.onNoteError("User not logged in", true)
        } else if (editNoteError) {
            noteListener.onNoteError("Error retrieving note", false)
        } else {
            noteListener.onNoteAvailable(currentNoteTitle, currentNoteContent)
        }
    }

    override fun removeNoteListener() {
        // No-op
    }

    override fun deleteNote(noteKey: String, deleteListener: (success: Boolean) -> Unit) {
        deleteListener(noteKey == VALID_NOTE_KEY)
    }

    override fun addNote(title: String, content: String) {
        currentNoteTitle = title
        currentNoteContent = content
    }

    override fun updateNote(title: String, content: String, createdAt: Long, noteKey: String) {
        currentNoteTitle = title
        currentNoteContent = content
    }

    var deleteAllUserNotesError = false

    override fun deleteAllUserNotes(onDeleteSuccess: () -> Unit, onDeleteError: () -> Unit) {
        if (deleteAllUserNotesError) {
            onDeleteError()
        } else {
            onDeleteSuccess()
        }
    }
}