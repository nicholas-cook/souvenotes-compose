package com.souvenotes.repository.user

enum class RegistrationState {
    Registered,
    EmailCollision,
    Error
}

enum class ForgotPasswordState {
    Sent,
    InvalidUser,
    Error,
}

enum class LoginState {
    LoggedIn,
    InvalidCredentials,
    InvalidUser,
    Error
}

enum class ReauthState {
    Reauthed,
    InvalidCredentials,
    Error
}

enum class UpdateEmailState {
    Updated,
    EmailCollision,
    Error
}

enum class UpdatePasswordState {
    Updated,
    Error
}

enum class DeleteUserState {
    Deleted,
    Error
}