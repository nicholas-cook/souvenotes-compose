package com.souvenotes.souvenotes.login.forgotpassword

import com.souvenotes.souvenotes.*
import com.souvenotes.souvenotes.repositories.FakeUserRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ForgotPasswordViewModelTest {

    private val userRepository = FakeUserRepository()

    private lateinit var viewModel: ForgotPasswordViewModel

    @Before
    fun setUp() {
        viewModel = ForgotPasswordViewModel(userRepository)
    }

    @Test
    fun `Test reset button enabled states`() {
        // Empty email
        viewModel.onEmailChanged("")
        Assert.assertEquals(false, viewModel.forgotPasswordScreenState.resetEnabled)

        // Email entered
        viewModel.onEmailChanged(VALID_EMAIL)
        Assert.assertEquals(true, viewModel.forgotPasswordScreenState.resetEnabled)
    }

    @Test
    fun `Test invalid email returns correct error message`() {
        viewModel.onEmailChanged(INVALID_EMAIL)
        viewModel.onResetClicked()
        Assert.assertEquals(R.string.email_format, viewModel.forgotPasswordScreenState.emailError)
    }

    @Test
    fun `Test email over 100 characters returns correct error message`() {
        viewModel.onEmailChanged(EMAIL_TOO_LONG)
        viewModel.onResetClicked()
        Assert.assertEquals(R.string.email_too_long, viewModel.forgotPasswordScreenState.emailError)
    }

    @Test
    fun `Test non-existing user returns correct error message`() {
        viewModel.onEmailChanged(ERROR_EMAIL)
        viewModel.onResetClicked()
        Assert.assertEquals(R.string.error_account, viewModel.forgotPasswordScreenState.resetError)
    }

    @Test
    fun `Test general forgot password error returns correct error message`() {
        viewModel.onEmailChanged(COLLISION_EMAIL)
        viewModel.onResetClicked()
        Assert.assertEquals(
            R.string.reset_password_error,
            viewModel.forgotPasswordScreenState.resetError
        )
    }

    @Test
    fun `Test valid password reset returns reset success`() {
        viewModel.onEmailChanged(VALID_EMAIL)
        viewModel.onResetClicked()
        Assert.assertEquals(true, viewModel.forgotPasswordScreenState.resetSuccess)
    }
}