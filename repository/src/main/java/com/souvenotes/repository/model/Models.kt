package com.souvenotes.repository.model

data class FirebaseNoteItem(val title: String = "", val content: String = "")

data class FirebaseNotesListItem(
    val title: String = "",
    val timestamp: Long = -1 * System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)

data class NotesListItem(
    val title: String = "",
    val dateTimeText: String = "",
    val key: String = "",
    val createdAt: Long = -1
)