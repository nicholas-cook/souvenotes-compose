package com.souvenotes.souvenotes.settings.email

import com.souvenotes.souvenotes.*
import com.souvenotes.souvenotes.repositories.FakeUserRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ChangeEmailViewModelTest {

    private val userRepository = FakeUserRepository()

    private lateinit var viewModel: ChangeEmailViewModel

    @Before
    fun setUp() {
        viewModel = ChangeEmailViewModel(userRepository)
    }

    @Test
    fun `Test submit button enabled states`() {
        // Initial state is disabled
        Assert.assertEquals(false, viewModel.changeEmailScreenState.submitEnabled)

        // Password entered
        viewModel.onEmailChanged(VALID_EMAIL)
        Assert.assertEquals(true, viewModel.changeEmailScreenState.submitEnabled)
    }

    @Test
    fun `Test invalid email returns email error`() {
        viewModel.onEmailChanged(INVALID_EMAIL)
        viewModel.onSubmitClicked()
        Assert.assertEquals(R.string.email_format, viewModel.changeEmailScreenState.emailError)
    }

    @Test
    fun `Test email over 100 characters returns correct error message`() {
        viewModel.onEmailChanged(EMAIL_TOO_LONG)
        viewModel.onSubmitClicked()
        Assert.assertEquals(
            R.string.email_length_message,
            viewModel.changeEmailScreenState.emailError
        )
    }

    @Test
    fun `Test valid email returns no email error`() {
        viewModel.onEmailChanged(VALID_EMAIL)
        viewModel.onSubmitClicked()
        Assert.assertEquals(null, viewModel.changeEmailScreenState.emailError)
    }

    @Test
    fun `Test valid email returns email change success`() {
        viewModel.onEmailChanged(VALID_EMAIL)
        viewModel.onSubmitClicked()
        Assert.assertEquals(true, viewModel.changeEmailScreenState.changeEmailSuccess)
    }

    @Test
    fun `Test changing to existing email returns correct error message`() {
        viewModel.onEmailChanged(COLLISION_EMAIL)
        viewModel.onSubmitClicked()
        Assert.assertEquals(
            R.string.email_exists,
            viewModel.changeEmailScreenState.changeEmailError
        )
    }

    @Test
    fun `Test general change email error returns correct error message`() {
        viewModel.onEmailChanged(ERROR_EMAIL)
        viewModel.onSubmitClicked()
        Assert.assertEquals(
            R.string.change_email_error,
            viewModel.changeEmailScreenState.changeEmailError
        )
    }
}