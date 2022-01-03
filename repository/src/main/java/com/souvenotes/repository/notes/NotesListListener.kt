package com.souvenotes.repository.notes

import com.souvenotes.repository.model.NotesListItem


interface NotesListListener {

    fun onNotesListAvailable(notesList: List<NotesListItem>)

    fun onNotesListError(message: String, toLogin: Boolean)
}