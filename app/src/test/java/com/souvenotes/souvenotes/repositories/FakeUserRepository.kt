package com.souvenotes.souvenotes.repositories

import com.souvenotes.repository.user.*
import com.souvenotes.souvenotes.*

class FakeUserRepository : UserRepository {

    private var isLoggedIn = false

    override fun isUserLoggedIn(): Boolean = isLoggedIn

    var refreshUserError = false

    override fun refreshUser(onRefreshComplete: (success: Boolean) -> Unit) {
        onRefreshComplete(!refreshUserError)
    }

    override fun getUserId(): String? {
        TODO("Not yet implemented")
    }

    override fun registerUser(
        email: String,
        password: String,
        onRegistrationResult: (registrationState: RegistrationState) -> Unit
    ) {
        if (email == VALID_EMAIL && password == VALID_PASSWORD) {
            onRegistrationResult(RegistrationState.Registered)
        } else if (email == COLLISION_EMAIL) {
            onRegistrationResult(RegistrationState.EmailCollision)
        } else {
            onRegistrationResult(RegistrationState.Error)
        }
    }

    override fun sendPasswordResetEmail(
        email: String,
        onSendResetResult: (forgotPasswordState: ForgotPasswordState) -> Unit
    ) {
        when (email) {
            VALID_EMAIL -> {
                onSendResetResult(ForgotPasswordState.Sent)
            }
            ERROR_EMAIL -> {
                onSendResetResult(ForgotPasswordState.InvalidUser)
            }
            else -> {
                onSendResetResult(ForgotPasswordState.Error)
            }
        }
    }

    override fun login(
        email: String,
        password: String,
        onLoginResult: (loginState: LoginState) -> Unit
    ) {
        if (email == VALID_EMAIL && password == VALID_PASSWORD) {
            isLoggedIn = true
            onLoginResult(LoginState.LoggedIn)
        } else if (email == ERROR_EMAIL) {
            onLoginResult(LoginState.InvalidUser)
        } else if (password == PASSWORD_TOO_SHORT) {
            onLoginResult(LoginState.InvalidCredentials)
        } else {
            onLoginResult(LoginState.Error)
        }
    }

    override fun reauthenticate(
        password: String,
        onReauthResult: (reauthState: ReauthState) -> Unit
    ) {
        when (password) {
            VALID_PASSWORD -> {
                onReauthResult(ReauthState.Reauthed)
            }
            PASSWORD_TOO_SHORT -> {
                onReauthResult(ReauthState.InvalidCredentials)
            }
            else -> {
                onReauthResult(ReauthState.Error)
            }
        }
    }

    override fun updateEmailAddress(
        email: String,
        onUpdateEmailResult: (updateEmailState: UpdateEmailState) -> Unit
    ) {
        when (email) {
            VALID_EMAIL -> {
                onUpdateEmailResult(UpdateEmailState.VerificationSent)
            }
            COLLISION_EMAIL -> {
                onUpdateEmailResult(UpdateEmailState.EmailCollision)
            }
            else -> {
                onUpdateEmailResult(UpdateEmailState.Error)
            }
        }
    }

    override fun updatePassword(
        password: String,
        onUpdatePasswordResult: (updatePasswordState: UpdatePasswordState) -> Unit
    ) {
        if (password == VALID_PASSWORD) {
            onUpdatePasswordResult(UpdatePasswordState.Updated)
        } else {
            onUpdatePasswordResult(UpdatePasswordState.Error)
        }
    }

    var deleteUserAccountError = false

    override fun deleteUser(onDeleteUserResult: (deleteUserState: DeleteUserState) -> Unit) {
        if (deleteUserAccountError) {
            onDeleteUserResult(DeleteUserState.Error)
        } else {
            onDeleteUserResult(DeleteUserState.Deleted)
        }
    }

    override fun logout() {
        isLoggedIn = false
    }
}