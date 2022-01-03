package com.souvenotes.repository.user

interface UserRepository {

    fun isUserLoggedIn(): Boolean

    fun refreshUser(onRefreshComplete: (success: Boolean) -> Unit)

    fun getUserId(): String?

    fun registerUser(
        email: String,
        password: String,
        onRegistrationResult: (registrationState: RegistrationState) -> Unit
    )

    fun sendPasswordResetEmail(
        email: String,
        onSendResetResult: (forgotPasswordState: ForgotPasswordState) -> Unit
    )

    fun login(email: String, password: String, onLoginResult: (loginState: LoginState) -> Unit)

    fun reauthenticate(
        password: String,
        onReauthResult: (reauthState: ReauthState) -> Unit
    )

    fun updateEmailAddress(
        email: String,
        onUpdateEmailResult: (updateEmailState: UpdateEmailState) -> Unit
    )

    fun updatePassword(
        password: String,
        onUpdatePasswordResult: (updatePasswordState: UpdatePasswordState) -> Unit
    )

    fun deleteUser(onDeleteUserResult: (deleteUserState: DeleteUserState) -> Unit)

    fun logout()
}