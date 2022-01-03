package com.souvenotes.souvenotes.notes

import com.souvenotes.souvenotes.*
import com.souvenotes.souvenotes.repositories.FakeNotesRepository
import com.souvenotes.souvenotes.repositories.FakeUserRepository
import org.junit.Assert
import org.junit.Test

class NotesListViewModelTest {

    private val userRepository = FakeUserRepository()
    private val notesRepository = FakeNotesRepository(userRepository)

    private lateinit var viewModel: NotesListViewModel

    private fun init(logout: Boolean = false, notesListError: Boolean = false) {
        if (logout) {
            userRepository.logout()
        } else {
            userRepository.login(VALID_EMAIL, VALID_PASSWORD) {}
            notesRepository.notesListError = notesListError
        }
        viewModel = NotesListViewModel(notesRepository, userRepository)
    }

    @Test
    fun `Test user is directed to login screen if accessing notes list while logged out`() {
        init(logout = true)
        Assert.assertEquals(true, viewModel.notesListScreenState.toLogin)
    }

    @Test
    fun `Test correct error is returned if there is an error loading notes`() {
        init(notesListError = true)
        Assert.assertEquals(R.string.load_notes_error, viewModel.notesListScreenState.notesError)
    }

    @Test
    fun `Test notes list is returned on successful list access`() {
        init()
        Assert.assertEquals(NOTES_LIST, viewModel.notesListScreenState.notes)
    }

    @Test
    fun `Test delete note error returns correct error message`() {
        init()
        viewModel.deleteNote(ERROR_NOTE_KEY)
        Assert.assertEquals(R.string.delete_error, viewModel.notesListScreenState.notesError)
    }

    @Test
    fun `Test logging out redirects user to login screen`() {
        init()
        viewModel.logout()
        Assert.assertEquals(true, viewModel.notesListScreenState.toLogin)
    }
}