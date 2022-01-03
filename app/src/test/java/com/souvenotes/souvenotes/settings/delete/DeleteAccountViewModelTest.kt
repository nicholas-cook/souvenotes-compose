package com.souvenotes.souvenotes.settings.delete

import com.souvenotes.souvenotes.repositories.FakeNotesRepository
import com.souvenotes.souvenotes.repositories.FakeUserRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DeleteAccountViewModelTest {

    private val userRepository = FakeUserRepository()
    private val notesRepository = FakeNotesRepository(userRepository)

    private lateinit var viewModel: DeleteAccountViewModel

    @Before
    fun setUp() {
        notesRepository.deleteAllUserNotesError = false
        userRepository.deleteUserAccountError = false
        viewModel = DeleteAccountViewModel(notesRepository, userRepository)
    }

    @Test
    fun `Test delete user notes error returns delete account error`() {
        notesRepository.deleteAllUserNotesError = true
        viewModel.onDeleteConfirmed()
        Assert.assertEquals(true, viewModel.deleteAccountScreenState.deleteError)
    }

    @Test
    fun `Test delete user account error returns delete account error`() {
        userRepository.deleteUserAccountError = true
        viewModel.onDeleteConfirmed()
        Assert.assertEquals(true, viewModel.deleteAccountScreenState.deleteError)
    }

    @Test
    fun `Test delete user account success`() {
        viewModel.onDeleteConfirmed()
        Assert.assertEquals(true, viewModel.deleteAccountScreenState.deleteSuccess)
    }
}