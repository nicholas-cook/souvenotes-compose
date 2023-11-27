package com.souvenotes.souvenotes.settings.email

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import com.souvenotes.repository.user.UpdateEmailState
import com.souvenotes.repository.user.UserRepository
import com.souvenotes.souvenotes.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    companion object {
        private const val EMAIL_MAX_LENGTH = 100
    }

    var changeEmailScreenState by mutableStateOf(ChangeEmailScreenState())
        private set

    fun onEmailChanged(email: String) {
        changeEmailScreenState =
            changeEmailScreenState.copy(email = email, submitEnabled = email.isNotBlank())
    }

    fun onSubmitClicked() {
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(changeEmailScreenState.email).matches()) {
            changeEmailScreenState = changeEmailScreenState.copy(emailError = R.string.email_format)
            return
        }
        if (changeEmailScreenState.email.length > EMAIL_MAX_LENGTH) {
            changeEmailScreenState =
                changeEmailScreenState.copy(emailError = R.string.email_length_message)
            return
        }
        changeEmailScreenState =
            changeEmailScreenState.copy(emailError = null, progressBarVisible = true)
        userRepository.updateEmailAddress(
            email = changeEmailScreenState.email,
            onUpdateEmailResult = { updateEmailState ->
                changeEmailScreenState = when (updateEmailState) {
                    UpdateEmailState.Updated -> changeEmailScreenState.copy(changeEmailSuccess = true)
                    UpdateEmailState.EmailCollision -> changeEmailScreenState.copy(
                        changeEmailError = R.string.email_exists,
                        progressBarVisible = false
                    )

                    UpdateEmailState.Error -> changeEmailScreenState.copy(
                        changeEmailError = R.string.change_email_error,
                        progressBarVisible = false
                    )
                }
            })
    }

    fun onErrorDismissed() {
        changeEmailScreenState = changeEmailScreenState.copy(changeEmailError = null)
    }
}