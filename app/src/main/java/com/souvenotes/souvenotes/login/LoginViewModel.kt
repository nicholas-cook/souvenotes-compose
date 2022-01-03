package com.souvenotes.souvenotes.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import com.souvenotes.repository.user.LoginState
import com.souvenotes.repository.user.UserRepository
import com.souvenotes.souvenotes.R

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    companion object {
        private const val EMAIL_MAX_LENGTH = 100
        private const val PASSWORD_MAX_LENGTH = 50
    }

    var loginsScreenState by mutableStateOf(LoginScreenState())
        private set

    init {
        if (userRepository.isUserLoggedIn()) {
            loginsScreenState = loginsScreenState.copy(loginSuccess = true)
        }
    }

    fun onEmailChanged(email: String?) {
        loginsScreenState = loginsScreenState.copy(email = email ?: "")
        checkEmailPassword()
    }

    fun onPasswordChanged(password: String?) {
        loginsScreenState = loginsScreenState.copy(password = password ?: "")
        checkEmailPassword()
    }

    fun onSubmitClicked() {
        var valid = true
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(loginsScreenState.email).matches()) {
            loginsScreenState = loginsScreenState.copy(emailError = R.string.email_format)
            valid = false
        }
        if (valid && loginsScreenState.email.length > EMAIL_MAX_LENGTH) {
            loginsScreenState = loginsScreenState.copy(emailError = R.string.email_too_long)
            valid = false
        }
        if (loginsScreenState.password.length > PASSWORD_MAX_LENGTH) {
            loginsScreenState = loginsScreenState.copy(passwordError = R.string.password_too_long)
            valid = false
        }
        if (!valid) {
            return
        }
        loginsScreenState = loginsScreenState.copy(
            emailError = null,
            passwordError = null,
            progressBarVisible = true
        )
        userRepository.login(
            email = loginsScreenState.email,
            password = loginsScreenState.password,
            onLoginResult = { loginState ->
                loginsScreenState = when (loginState) {
                    LoginState.LoggedIn -> loginsScreenState.copy(loginSuccess = true)
                    LoginState.InvalidCredentials -> loginsScreenState.copy(
                        loginError = R.string.error_credentials,
                        progressBarVisible = false
                    )
                    LoginState.InvalidUser -> loginsScreenState.copy(
                        loginError = R.string.error_account,
                        progressBarVisible = false
                    )
                    LoginState.Error -> loginsScreenState.copy(
                        loginError = R.string.login_error,
                        progressBarVisible = false
                    )
                }
            })
    }

    fun onLoginErrorDismissed() {
        loginsScreenState = loginsScreenState.copy(loginError = null)
    }

    private fun checkEmailPassword() {
        val submitEnabled = loginsScreenState.email.isNotBlank()
                && loginsScreenState.password.isNotEmpty()
        loginsScreenState = loginsScreenState.copy(submitEnabled = submitEnabled)
    }
}