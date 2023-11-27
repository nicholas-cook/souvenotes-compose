package com.souvenotes.repository.user

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor() : UserRepository {

    override fun isUserLoggedIn(): Boolean = FirebaseAuth.getInstance().currentUser != null

    override fun refreshUser(onRefreshComplete: (success: Boolean) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)
            ?.addOnCompleteListener { task -> onRefreshComplete(task.isSuccessful) }
            ?: onRefreshComplete(false)
    }

    override fun getUserId(): String? = FirebaseAuth.getInstance().currentUser?.uid

    override fun registerUser(
        email: String,
        password: String,
        onRegistrationResult: (registrationState: RegistrationState) -> Unit
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onRegistrationResult(RegistrationState.Registered)
                } else {
                    when (task.exception) {
                        is FirebaseAuthUserCollisionException -> onRegistrationResult(
                            RegistrationState.EmailCollision
                        )

                        else -> onRegistrationResult(RegistrationState.Error)
                    }
                }
            }
    }

    override fun sendPasswordResetEmail(
        email: String,
        onSendResetResult: (forgotPasswordState: ForgotPasswordState) -> Unit
    ) {
        with(FirebaseAuth.getInstance()) {
            useAppLanguage()
            sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSendResetResult(ForgotPasswordState.Sent)
                } else {
                    when (task.exception) {
                        is FirebaseAuthInvalidUserException -> onSendResetResult(ForgotPasswordState.InvalidUser)
                        else -> onSendResetResult(ForgotPasswordState.Error)
                    }
                }
            }
        }
    }

    override fun login(
        email: String,
        password: String,
        onLoginResult: (loginState: LoginState) -> Unit
    ) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onLoginResult(LoginState.LoggedIn)
                } else {
                    val state = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> LoginState.InvalidUser
                        is FirebaseAuthInvalidCredentialsException -> LoginState.InvalidCredentials
                        else -> LoginState.Error
                    }
                    onLoginResult(state)
                }
            }
    }

    override fun reauthenticate(
        password: String,
        onReauthResult: (reauthState: ReauthState) -> Unit
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val credential = EmailAuthProvider.getCredential(user.email ?: "", password)
            user.reauthenticate(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onReauthResult(ReauthState.Reauthed)
                } else {
                    when (task.exception) {
                        is FirebaseAuthInvalidCredentialsException -> onReauthResult(ReauthState.InvalidCredentials)
                        else -> onReauthResult(ReauthState.Error)
                    }
                }
            }
        } else {
            onReauthResult(ReauthState.Error)
        }
    }

    override fun updateEmailAddress(
        email: String,
        onUpdateEmailResult: (updateEmailState: UpdateEmailState) -> Unit
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.updateEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onUpdateEmailResult(UpdateEmailState.Updated)
                } else {
                    when (task.exception) {
                        is FirebaseAuthUserCollisionException -> onUpdateEmailResult(
                            UpdateEmailState.EmailCollision
                        )

                        else -> onUpdateEmailResult(UpdateEmailState.Error)
                    }
                }
            }
        } else {
            onUpdateEmailResult(UpdateEmailState.Error)
        }
    }

    override fun updatePassword(
        password: String,
        onUpdatePasswordResult: (updatePasswordState: UpdatePasswordState) -> Unit
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.updatePassword(password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onUpdatePasswordResult(UpdatePasswordState.Updated)
                } else {
                    onUpdatePasswordResult(UpdatePasswordState.Error)
                }
            }
        } else {
            onUpdatePasswordResult(UpdatePasswordState.Error)
        }
    }

    override fun deleteUser(onDeleteUserResult: (deleteUserState: DeleteUserState) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onDeleteUserResult(DeleteUserState.Deleted)
                } else {
                    onDeleteUserResult(DeleteUserState.Error)
                }
            }
        } else {
            onDeleteUserResult(DeleteUserState.Error)
        }
    }

    override fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}