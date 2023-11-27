package com.souvenotes.souvenotes.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.souvenotes.repository.model.NotesListItem
import com.souvenotes.repository.notes.NotesListListener
import com.souvenotes.repository.notes.NotesRepository
import com.souvenotes.repository.user.UserRepository
import com.souvenotes.souvenotes.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var notesListScreenState by mutableStateOf(NotesListScreenState())
        private set

    init {
        getNotes()
    }

    fun logout() {
        userRepository.logout()
        notesListScreenState = notesListScreenState.copy(toLogin = true)
    }

    fun onNotesErrorDismissed() {
        notesListScreenState = notesListScreenState.copy(notesError = null)
    }

    fun deleteNote(noteKey: String) {
        notesRepository.deleteNote(noteKey) { success ->
            if (!success) {
                notesListScreenState = notesListScreenState.copy(notesError = R.string.delete_error)
            }
        }
    }

    private fun getNotes() {
        notesRepository.addNotesListListener(object : NotesListListener {
            override fun onNotesListAvailable(notesList: List<NotesListItem>) {
                notesListScreenState = notesListScreenState.copy(notes = notesList)
            }

            override fun onNotesListError(message: String, toLogin: Boolean) {
                notesListScreenState = notesListScreenState.copy(
                    notesError = R.string.load_notes_error,
                    toLogin = toLogin
                )
            }
        })
    }

    override fun onCleared() {
        notesRepository.removeNotesListListener()
    }
}