package com.souvenotes.souvenotes.notes.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.souvenotes.repository.notes.NoteListener
import com.souvenotes.repository.notes.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val TITLE_MAX_LENGTH = 200
        private const val CONTENT_MAX_LENGTH = 25000
    }

    private var noteKey: String = savedStateHandle["noteKey"] ?: ""
    private val createdAt: Long = savedStateHandle["createdAt"] ?: -1L

    var editNoteScreenState by mutableStateOf(EditNoteScreenState(isNewNote = noteKey.isEmpty()))
        private set

    private var existingTitle = ""
    private var existingContent = ""

    private var isDeleting = false

    init {
        if (noteKey.isNotEmpty()) {
            notesRepository.addNoteListener(noteKey, object : NoteListener {
                override fun onNoteAvailable(title: String, content: String) {
                    existingTitle = title
                    existingContent = content
                    editNoteScreenState = editNoteScreenState.copy(title = title, content = content)
                }

                override fun onNoteError(message: String, toLogin: Boolean) {
                    if (!isDeleting) {
                        editNoteScreenState =
                            editNoteScreenState.copy(loadNoteError = true, toList = true)
                    }
                }
            })
        }
    }

    fun onTitleChanged(title: String) {
        editNoteScreenState = editNoteScreenState.copy(
            title = title,
            titleLengthError = title.length > TITLE_MAX_LENGTH
        )
    }

    fun onContentChanged(content: String) {
        editNoteScreenState = editNoteScreenState.copy(
            content = content,
            contentLengthError = content.length > CONTENT_MAX_LENGTH
        )
    }

    fun saveNote() {
        if (isDeleting) {
            return
        }
        if (existingTitle == editNoteScreenState.title && existingContent == editNoteScreenState.content) {
            return
        }
        if (noteKey.isNotEmpty()) {
            notesRepository.updateNote(
                editNoteScreenState.title,
                editNoteScreenState.content,
                createdAt,
                noteKey
            )
        } else {
            // Update note key to prevent duplicate notes
            noteKey =
                notesRepository.addNote(editNoteScreenState.title, editNoteScreenState.content)
        }
    }

    fun deleteNote() {
        isDeleting = true
        if (noteKey.isNotEmpty()) {
            notesRepository.deleteNote(noteKey) { success ->
                editNoteScreenState = if (success) {
                    editNoteScreenState.copy(toList = true)
                } else {
                    isDeleting = false
                    editNoteScreenState.copy(deleteNoteError = true)
                }
            }
        } else {
            editNoteScreenState = editNoteScreenState.copy(toList = true)
        }
    }

    fun onErrorDismissed() {
        editNoteScreenState = editNoteScreenState.copy(deleteNoteError = false)
    }

    override fun onCleared() {
        notesRepository.removeNoteListener()
    }
}