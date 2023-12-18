package com.souvenotes.souvenotes.settings.email

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import com.souvenotes.repository.user.UpdateEmailState
import com.souvenotes.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    companion object {
        private const val EMAIL_MAX_LENGTH = 100
    }

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _changeEmailScreenState =
        MutableStateFlow<ChangeEmailScreenState>(ChangeEmailScreenState.Initial)
    val changeEmailScreenState: StateFlow<ChangeEmailScreenState> =
        _changeEmailScreenState.asStateFlow()

    fun onEmailChanged(email: String) {
        _email.value = email
    }

    fun onSubmitClicked() {
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(_email.value).matches()) {
            _changeEmailScreenState.value = ChangeEmailScreenState.EmailFormatError
            return
        }
        if (_email.value.length > EMAIL_MAX_LENGTH) {
            _changeEmailScreenState.value = ChangeEmailScreenState.EmailLengthError
            return
        }
        _changeEmailScreenState.value = ChangeEmailScreenState.Loading
        userRepository.updateEmailAddress(
            email = _email.value,
            onUpdateEmailResult = { updateEmailState ->
                _changeEmailScreenState.value = when (updateEmailState) {
                    UpdateEmailState.VerificationSent -> ChangeEmailScreenState.Success
                    UpdateEmailState.EmailCollision -> ChangeEmailScreenState.EmailCollisionError
                    UpdateEmailState.Error -> ChangeEmailScreenState.Error
                }
            })
    }

    fun onErrorDismissed() {
        _changeEmailScreenState.value = ChangeEmailScreenState.Initial
    }
}