package com.souvenotes.souvenotes.login

import com.souvenotes.souvenotes.*
import com.souvenotes.souvenotes.repositories.FakeUserRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    private val userRepository = FakeUserRepository()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewModel = LoginViewModel(userRepository)
    }

    @Test
    fun `Test submit button enabled states`() {
        // Empty email
        viewModel.onEmailChanged("")
        viewModel.onPasswordChanged(VALID_PASSWORD)
        Assert.assertEquals(false, viewModel.loginsScreenState.submitEnabled)

        // Empty password
        viewModel.onEmailChanged(VALID_EMAIL)
        viewModel.onPasswordChanged("")
        Assert.assertEquals(false, viewModel.loginsScreenState.submitEnabled)

        // Both fields entered
        viewModel.onEmailChanged(VALID_EMAIL)
        viewModel.onPasswordChanged(VALID_PASSWORD)
        Assert.assertEquals(true, viewModel.loginsScreenState.submitEnabled)
    }

    @Test
    fun `Test invalid email returns correct error message`() {
        viewModel.onEmailChanged(INVALID_EMAIL)
        viewModel.onSubmitClicked()
        Assert.assertEquals(R.string.email_format, viewModel.loginsScreenState.emailError)
    }

    @Test
    fun `Test email over 100 characters returns correct error message`() {
        viewModel.onEmailChanged(EMAIL_TOO_LONG)
        viewModel.onSubmitClicked()
        Assert.assertEquals(R.string.email_too_long, viewModel.loginsScreenState.emailError)
    }

    @Test
    fun `Test password over 50 characters returns correct error message`() {
        viewModel.onPasswordChanged(PASSWORD_TOO_LONG)
        viewModel.onSubmitClicked()
        Assert.assertEquals(R.string.password_too_long, viewModel.loginsScreenState.passwordError)
    }

    @Test
    fun `Test non-existing user returns correct error message`() {
        viewModel.onEmailChanged(ERROR_EMAIL)
        viewModel.onSubmitClicked()
        Assert.assertEquals(R.string.error_account, viewModel.loginsScreenState.loginError)
    }

    @Test
    fun `Test invalid credentials returns correct error message`() {
        viewModel.onEmailChanged(VALID_EMAIL)
        viewModel.onPasswordChanged(PASSWORD_TOO_SHORT)
        viewModel.onSubmitClicked()
        Assert.assertEquals(R.string.error_credentials, viewModel.loginsScreenState.loginError)
    }

    @Test
    fun `Test general login error returns correct error message`() {
        viewModel.onEmailChanged(VALID_EMAIL)
        viewModel.onPasswordChanged(PASSWORD_NO_NUMBERS)
        viewModel.onSubmitClicked()
        Assert.assertEquals(R.string.login_error, viewModel.loginsScreenState.loginError)
    }

    @Test
    fun `Test valid email and password returns login success`() {
        viewModel.onEmailChanged(VALID_EMAIL)
        viewModel.onPasswordChanged(VALID_PASSWORD)
        viewModel.onSubmitClicked()
        Assert.assertEquals(true, viewModel.loginsScreenState.loginSuccess)
    }
}