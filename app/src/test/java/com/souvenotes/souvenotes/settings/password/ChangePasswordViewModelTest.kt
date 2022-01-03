package com.souvenotes.souvenotes.settings.password

import com.souvenotes.souvenotes.*
import com.souvenotes.souvenotes.repositories.FakeUserRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ChangePasswordViewModelTest {

    private val userRepository = FakeUserRepository()

    private lateinit var viewModel: ChangePasswordViewModel

    @Before
    fun setUp() {
        viewModel = ChangePasswordViewModel(userRepository)
    }

    @Test
    fun `Test submit button enabled states`() {
        // Initial state is disabled
        Assert.assertEquals(false, viewModel.changePasswordScreenState.submitEnabled)

        // Password entered
        viewModel.onPasswordChanged(VALID_PASSWORD)
        Assert.assertEquals(true, viewModel.changePasswordScreenState.submitEnabled)
    }

    @Test
    fun `Test invalid passwords return correct error message`() {
        // Too short
        viewModel.onPasswordChanged(PASSWORD_TOO_SHORT)
        viewModel.onSubmitClicked()
        Assert.assertEquals(
            R.string.password_short,
            viewModel.changePasswordScreenState.passwordError
        )

        // No letters
        viewModel.onPasswordChanged(PASSWORD_NO_LETTERS)
        viewModel.onSubmitClicked()
        Assert.assertEquals(
            R.string.password_missing_letter,
            viewModel.changePasswordScreenState.passwordError
        )

        // No numbers
        viewModel.onPasswordChanged(PASSWORD_NO_NUMBERS)
        viewModel.onSubmitClicked()
        Assert.assertEquals(
            R.string.password_missing_number,
            viewModel.changePasswordScreenState.passwordError
        )

        // Password over 50 characters
        viewModel.onPasswordChanged(PASSWORD_TOO_LONG)
        viewModel.onSubmitClicked()
        Assert.assertEquals(
            R.string.password_too_long,
            viewModel.changePasswordScreenState.passwordError
        )
    }

    @Test
    fun `Test valid password returns null error message`() {
        viewModel.onPasswordChanged(VALID_PASSWORD)
        viewModel.onSubmitClicked()
        Assert.assertEquals(null, viewModel.changePasswordScreenState.passwordError)
    }

    @Test
    fun `Test valid password returns password change success`() {
        viewModel.onPasswordChanged(VALID_PASSWORD)
        viewModel.onSubmitClicked()
        Assert.assertEquals(true, viewModel.changePasswordScreenState.changePasswordSuccess)
    }

    @Test
    fun `Test general change password error returns correct error message`() {
        viewModel.onPasswordChanged(ERROR_PASSWORD)
        viewModel.onSubmitClicked()
        Assert.assertEquals(
            R.string.change_password_error,
            viewModel.changePasswordScreenState.changePasswordError
        )
    }
}