package com.souvenotes.repository.notes

interface NoteListener {

    fun onNoteAvailable(title: String, content: String)

    fun onNoteError(message: String, toLogin: Boolean)
}