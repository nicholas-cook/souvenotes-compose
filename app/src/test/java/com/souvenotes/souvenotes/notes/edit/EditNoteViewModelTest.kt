package com.souvenotes.souvenotes.notes.edit

import com.souvenotes.souvenotes.*
import com.souvenotes.souvenotes.repositories.FakeNotesRepository
import com.souvenotes.souvenotes.repositories.FakeUserRepository
import org.junit.Assert
import org.junit.Test

class EditNoteViewModelTest {

    private val userRepository = FakeUserRepository()
    private val notesRepository = FakeNotesRepository(userRepository)

    private lateinit var viewModel: EditNoteViewModel

    private fun init(logout: Boolean = false, noteKey: String, editNoteError: Boolean = false) {
        if (logout) {
            userRepository.logout()
        } else {
            userRepository.login(VALID_EMAIL, VALID_PASSWORD) {}
            notesRepository.editNoteError = editNoteError
        }
        viewModel = EditNoteViewModel(noteKey, System.currentTimeMillis(), notesRepository)
    }

    @Test
    fun `Test new note is properly loaded`() {
        init(noteKey = "")
        Assert.assertEquals(true, viewModel.editNoteScreenState.isNewNote)
    }

    @Test
    fun `Test existing note is properly loaded`() {
        init(noteKey = VALID_NOTE_KEY)
        Assert.assertEquals(NOTE_TITLE, viewModel.editNoteScreenState.title)
        Assert.assertEquals(NOTE_CONTENT, viewModel.editNoteScreenState.content)
    }

    @Test
    fun `Test title over 200 characters returns title length error`() {
        init(noteKey = VALID_NOTE_KEY)
        viewModel.onTitleChanged(TITLE_TOO_LONG)
        Assert.assertEquals(true, viewModel.editNoteScreenState.titleLengthError)
    }

    @Test
    fun `Test content length over 25,000 returns content length error`() {
        init(noteKey = VALID_NOTE_KEY)
        viewModel.onContentChanged(CONTENT_TOO_LONG)
        Assert.assertEquals(true, viewModel.editNoteScreenState.contentLengthError)
    }

    @Test
    fun `Test logged out user redirected to list screen`() {
        init(logout = true, noteKey = VALID_NOTE_KEY)
        Assert.assertEquals(true, viewModel.editNoteScreenState.toList)
    }

    @Test
    fun `Test user is redirected to list screen if there is an error loading note`() {
        init(noteKey = VALID_NOTE_KEY, editNoteError = true)
        Assert.assertEquals(true, viewModel.editNoteScreenState.toList)
    }

    @Test
    fun `Test successfully deleting existing note redirects user to list screen`() {
        init(noteKey = VALID_NOTE_KEY)
        viewModel.deleteNote()
        Assert.assertEquals(true, viewModel.editNoteScreenState.toList)
    }

    @Test
    fun `Test successfully deleting new note redirects user to list screen`() {
        init(noteKey = "")
        viewModel.deleteNote()
        Assert.assertEquals(true, viewModel.editNoteScreenState.toList)
    }

    @Test
    fun `Test error deleting note returns delete note error`() {
        init(noteKey = ERROR_NOTE_KEY)
        viewModel.deleteNote()
        Assert.assertEquals(true, viewModel.editNoteScreenState.deleteNoteError)
    }

    @Test
    fun `Test saving same values does not change stored values`() {
        init(noteKey = VALID_NOTE_KEY)
        Assert.assertEquals(NOTE_TITLE, viewModel.editNoteScreenState.title)
        Assert.assertEquals(NOTE_CONTENT, viewModel.editNoteScreenState.content)
        viewModel.onTitleChanged(NOTE_TITLE)
        viewModel.onContentChanged(NOTE_CONTENT)
        viewModel.saveNote()
        Assert.assertEquals(NOTE_TITLE, notesRepository.currentNoteTitle)
        Assert.assertEquals(NOTE_CONTENT, notesRepository.currentNoteContent)
    }

    @Test
    fun `Test saving updated values changes stored values`() {
        init(noteKey = VALID_NOTE_KEY)
        Assert.assertEquals(NOTE_TITLE, viewModel.editNoteScreenState.title)
        Assert.assertEquals(NOTE_CONTENT, viewModel.editNoteScreenState.content)
        viewModel.onTitleChanged(UPDATED_NOTE_TITLE)
        viewModel.onContentChanged(UPDATED_NOTE_CONTENT)
        viewModel.saveNote()
        Assert.assertEquals(UPDATED_NOTE_TITLE, notesRepository.currentNoteTitle)
        Assert.assertEquals(UPDATED_NOTE_CONTENT, notesRepository.currentNoteContent)
    }

    @Test
    fun `Test saving new note creates new stored values`() {
        init(noteKey = "")
        Assert.assertEquals("", viewModel.editNoteScreenState.title)
        Assert.assertEquals("", viewModel.editNoteScreenState.content)
        viewModel.onTitleChanged(UPDATED_NOTE_TITLE)
        viewModel.onContentChanged(UPDATED_NOTE_CONTENT)
        viewModel.saveNote()
        Assert.assertEquals(UPDATED_NOTE_TITLE, notesRepository.currentNoteTitle)
        Assert.assertEquals(UPDATED_NOTE_CONTENT, notesRepository.currentNoteContent)
    }
}