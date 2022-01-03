package com.souvenotes.souvenotes.settings.reauth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.souvenotes.repository.user.ReauthState
import com.souvenotes.repository.user.UserRepository
import com.souvenotes.souvenotes.R

class ReauthViewModel(private val userRepository: UserRepository) : ViewModel() {

    companion object {
        private const val PASSWORD_MAX_LENGTH = 50
    }

    var reauthScreenState by mutableStateOf(ReauthScreenState())
        private set

    fun onPasswordChanged(password: String) {
        reauthScreenState =
            reauthScreenState.copy(password = password, submitEnabled = password.isNotEmpty())
    }

    fun onSubmitClicked() {
        if (reauthScreenState.password.length > PASSWORD_MAX_LENGTH) {
            reauthScreenState = reauthScreenState.copy(passwordError = R.string.password_too_long)
            return
        }
        reauthScreenState = reauthScreenState.copy(passwordError = null, progressBarVisible = true)
        userRepository.reauthenticate(
            password = reauthScreenState.password,
            onReauthResult = { reauthState ->
                reauthScreenState = when (reauthState) {
                    ReauthState.Reauthed -> reauthScreenState.copy(reauthSuccess = true)
                    ReauthState.InvalidCredentials -> reauthScreenState.copy(
                        reauthError = R.string.error_credentials,
                        progressBarVisible = false
                    )
                    ReauthState.Error -> reauthScreenState.copy(
                        reauthError = R.string.reauth_error,
                        progressBarVisible = false
                    )
                }
            }
        )
    }

    fun onErrorDismissed() {
        reauthScreenState = reauthScreenState.copy(reauthError = null)
    }
}