package com.souvenotes.repository.notes

interface NotesRepository {

    fun addNotesListListener(notesListListener: NotesListListener)

    fun removeNotesListListener()

    fun addNoteListener(noteKey: String, noteListener: NoteListener)

    fun removeNoteListener()

    fun deleteNote(noteKey: String, deleteListener: (success: Boolean) -> Unit)

    fun addNote(title: String, content: String)

    fun updateNote(title: String, content: String, createdAt: Long, noteKey: String)

    fun deleteAllUserNotes(onDeleteSuccess: () -> Unit, onDeleteError: () -> Unit)
}