package com.souvenotes.souvenotes.settings.delete

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.souvenotes.repository.notes.NotesRepository
import com.souvenotes.repository.user.DeleteUserState
import com.souvenotes.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var deleteAccountScreenState by mutableStateOf(DeleteAccountScreenState())
        private set

    fun onDeleteConfirmed() {
        deleteAccountScreenState = deleteAccountScreenState.copy(progressBarVisible = true)
        notesRepository.deleteAllUserNotes(
            onDeleteSuccess = { deleteUserAccount() },
            onDeleteError = {
                deleteAccountScreenState =
                    deleteAccountScreenState.copy(deleteError = true, progressBarVisible = false)
            })
    }

    private fun deleteUserAccount() {
        userRepository.deleteUser { deleteUserState ->
            deleteAccountScreenState = when (deleteUserState) {
                DeleteUserState.Deleted -> deleteAccountScreenState.copy(deleteSuccess = true)
                DeleteUserState.Error -> deleteAccountScreenState.copy(
                    deleteError = true,
                    progressBarVisible = false
                )
            }
        }
    }

    fun onErrorDismissed() {
        deleteAccountScreenState = deleteAccountScreenState.copy(deleteError = false)
    }
}