package com.souvenotes.souvenotes.settings.reauth

import androidx.lifecycle.ViewModel
import com.souvenotes.repository.user.ReauthState
import com.souvenotes.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ReauthViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    companion object {
        private const val PASSWORD_MAX_LENGTH = 50
    }

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _reauthScreenState =
        MutableStateFlow<ReauthScreenState>(ReauthScreenState.Initial)
    val reauthScreenState: StateFlow<ReauthScreenState> = _reauthScreenState.asStateFlow()

    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    fun onSubmitClicked() {
        if (_password.value.length > PASSWORD_MAX_LENGTH) {
            _reauthScreenState.value = ReauthScreenState.PasswordLengthError
            return
        }
        _reauthScreenState.value = ReauthScreenState.Loading
        userRepository.reauthenticate(
            password = _password.value,
            onReauthResult = { reauthState ->
                _reauthScreenState.value = when (reauthState) {
                    ReauthState.Reauthed -> ReauthScreenState.ReauthSuccess
                    ReauthState.InvalidCredentials -> ReauthScreenState.CredentialsError
                    ReauthState.Error -> ReauthScreenState.ReauthError
                }
            }
        )
    }

    fun onErrorDismissed() {
        _reauthScreenState.value = ReauthScreenState.Initial
    }
}