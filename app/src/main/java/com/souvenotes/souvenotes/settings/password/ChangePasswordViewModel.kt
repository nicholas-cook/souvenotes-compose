package com.souvenotes.souvenotes.settings.password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.souvenotes.repository.user.UpdatePasswordState
import com.souvenotes.repository.user.UserRepository
import com.souvenotes.souvenotes.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
        private const val PASSWORD_MAX_LENGTH = 50
    }

    var changePasswordScreenState by mutableStateOf(ChangePasswordScreenState())
        private set

    fun onPasswordChanged(password: String) {
        changePasswordScreenState = changePasswordScreenState.copy(
            password = password,
            submitEnabled = password.isNotEmpty()
        )
    }

    fun onSubmitClicked() {
        var validationState = changePasswordScreenState
        var valid = true
        val password = validationState.password
        if (password.length < PASSWORD_MIN_LENGTH) {
            valid = false
            validationState = validationState.copy(passwordError = R.string.password_short)
        } else if (password.length > PASSWORD_MAX_LENGTH) {
            valid = false
            validationState = validationState.copy(passwordError = R.string.password_too_long)
        } else if (!Regex("[A-Za-z]+").containsMatchIn(password)) {
            valid = false
            validationState = validationState.copy(passwordError = R.string.password_missing_letter)
        } else if (!Regex("[0-9]+").containsMatchIn(password)) {
            valid = false
            validationState = validationState.copy(passwordError = R.string.password_missing_number)
        } else {
            validationState = validationState.copy(passwordError = null)
        }

        if (valid) {
            changePasswordScreenState = validationState.copy(progressBarVisible = true)
            userRepository.updatePassword(
                password = password,
                onUpdatePasswordResult = { updatePasswordState ->
                    changePasswordScreenState = when (updatePasswordState) {
                        UpdatePasswordState.Updated -> changePasswordScreenState.copy(
                            changePasswordSuccess = true
                        )

                        UpdatePasswordState.Error -> changePasswordScreenState.copy(
                            changePasswordError = R.string.change_password_error,
                            progressBarVisible = false
                        )
                    }
                }
            )
        } else {
            changePasswordScreenState = validationState
        }
    }

    fun onErrorDismissed() {
        changePasswordScreenState = changePasswordScreenState.copy(changePasswordError = null)
    }
}