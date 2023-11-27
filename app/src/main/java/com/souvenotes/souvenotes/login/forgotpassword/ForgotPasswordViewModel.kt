package com.souvenotes.souvenotes.login.forgotpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import com.souvenotes.repository.user.ForgotPasswordState
import com.souvenotes.repository.user.UserRepository
import com.souvenotes.souvenotes.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    companion object {
        private const val EMAIL_MAX_LENGTH = 100
    }

    var forgotPasswordScreenState by mutableStateOf(ForgotPasswordScreenState())
        private set

    fun onEmailChanged(email: String?) {
        forgotPasswordScreenState = forgotPasswordScreenState.copy(email = email ?: "")
        checkEmail()
    }

    fun onResetClicked() {
        val email = forgotPasswordScreenState.email
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            forgotPasswordScreenState =
                forgotPasswordScreenState.copy(emailError = R.string.email_format)
            return
        }
        if (email.length > EMAIL_MAX_LENGTH) {
            forgotPasswordScreenState =
                forgotPasswordScreenState.copy(emailError = R.string.email_too_long)
            return
        }
        forgotPasswordScreenState =
            forgotPasswordScreenState.copy(emailError = null, progressBarVisible = true)
        userRepository.sendPasswordResetEmail(
            email = email,
            onSendResetResult = { forgotPasswordState ->
                forgotPasswordScreenState = when (forgotPasswordState) {
                    ForgotPasswordState.Sent -> forgotPasswordScreenState.copy(
                        resetSuccess = true,
                        progressBarVisible = false
                    )

                    ForgotPasswordState.InvalidUser -> forgotPasswordScreenState.copy(
                        resetError = R.string.error_account,
                        progressBarVisible = false
                    )

                    ForgotPasswordState.Error -> forgotPasswordScreenState.copy(
                        resetError = R.string.reset_password_error,
                        progressBarVisible = false
                    )
                }
            })
    }

    fun onResetErrorDismissed() {
        forgotPasswordScreenState = forgotPasswordScreenState.copy(resetError = null)
    }

    private fun checkEmail() {
        val isResetEnabled = forgotPasswordScreenState.email.isNotBlank()
        forgotPasswordScreenState = forgotPasswordScreenState.copy(resetEnabled = isResetEnabled)
    }
}