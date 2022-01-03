package com.souvenotes.souvenotes.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import com.souvenotes.repository.user.RegistrationState
import com.souvenotes.repository.user.UserRepository
import com.souvenotes.souvenotes.R

class RegistrationViewModel(private val userRepository: UserRepository) : ViewModel() {

    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
        private const val PASSWORD_MAX_LENGTH = 50
        private const val EMAIL_MAX_LENGTH = 100
    }

    var registrationScreenState by mutableStateOf(RegistrationScreenState())
        private set

    fun onEmailChanged(email: String?) {
        registrationScreenState = registrationScreenState.copy(email = email ?: "")
        checkEmailPassword()
    }

    fun onPasswordChanged(password: String?) {
        registrationScreenState = registrationScreenState.copy(password = password ?: "")
        checkEmailPassword()
    }

    fun onConfirmPasswordChanged(confirmPassword: String?) {
        registrationScreenState =
            registrationScreenState.copy(confirmPassword = confirmPassword ?: "")
        checkEmailPassword()
    }

    fun onSignUpClicked() {
        val email = registrationScreenState.email
        val password = registrationScreenState.password
        val confirmPassword = registrationScreenState.confirmPassword

        var validationState = registrationScreenState
        var valid = true
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false
            validationState = validationState.copy(emailError = R.string.email_format)
        } else if (email.length > EMAIL_MAX_LENGTH) {
            valid = false
            validationState = validationState.copy(emailError = R.string.email_length_message)
        } else {
            validationState = validationState.copy(emailError = null)
        }

        if (password.length < 8) {
            valid = false
            validationState = validationState.copy(passwordError = R.string.password_short)
        } else if (password.length > 50) {
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

        if (password != confirmPassword) {
            valid = false
            validationState =
                validationState.copy(confirmPasswordError = R.string.password_match_error)
        } else {
            validationState = validationState.copy(confirmPasswordError = null)
        }

        if (valid) {
            registrationScreenState = validationState.copy(progressBarVisible = true)
            userRepository.registerUser(
                email = email,
                password = password,
                onRegistrationResult = { registrationState ->
                    registrationScreenState = when (registrationState) {
                        RegistrationState.Registered -> registrationScreenState.copy(
                            registrationSuccess = true
                        )
                        RegistrationState.EmailCollision -> registrationScreenState.copy(
                            progressBarVisible = false,
                            registrationError = R.string.email_exists
                        )
                        RegistrationState.Error -> registrationScreenState.copy(
                            progressBarVisible = false,
                            registrationError = R.string.registration_error
                        )
                    }
                })
        } else {
            registrationScreenState = validationState
        }
    }

    fun onErrorDismissed() {
        registrationScreenState = registrationScreenState.copy(registrationError = null)
    }

    private fun checkEmailPassword() {
        val isSignUpEnabled = registrationScreenState.email.isNotBlank()
                && registrationScreenState.password.isNotEmpty()
                && registrationScreenState.confirmPassword.isNotEmpty()
        registrationScreenState = registrationScreenState.copy(signUpEnabled = isSignUpEnabled)
    }
}