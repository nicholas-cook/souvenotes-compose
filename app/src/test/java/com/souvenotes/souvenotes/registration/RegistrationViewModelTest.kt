package com.souvenotes.souvenotes.registration

import com.souvenotes.souvenotes.*
import com.souvenotes.souvenotes.repositories.FakeUserRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RegistrationViewModelTest {

    private val userRepository = FakeUserRepository()

    private lateinit var viewModel: RegistrationViewModel

    @Before
    fun setUp() {
        viewModel = RegistrationViewModel(userRepository)
    }

    @Test
    fun `Test sign up button enabled states`() {
        // Initial state is disabled
        Assert.assertEquals(false, viewModel.registrationScreenState.signUpEnabled)

        // Empty email
        viewModel.onEmailChanged("")
        viewModel.onPasswordChanged(VALID_PASSWORD)
        viewModel.onConfirmPasswordChanged(VALID_PASSWORD)
        Assert.assertEquals(false, viewModel.registrationScreenState.signUpEnabled)

        // Empty password
        viewModel.onEmailChanged(VALID_EMAIL)
        viewModel.onPasswordChanged("")
        viewModel.onConfirmPasswordChanged(VALID_PASSWORD)
        Assert.assertEquals(false, viewModel.registrationScreenState.signUpEnabled)

        // Empty confirm password
        viewModel.onEmailChanged(VALID_EMAIL)
        viewModel.onPasswordChanged(VALID_PASSWORD)
        viewModel.onConfirmPasswordChanged("")
        Assert.assertEquals(false, viewModel.registrationScreenState.signUpEnabled)

        // All fields entered
        viewModel.onEmailChanged(VALID_EMAIL)
        viewModel.onPasswordChanged(VALID_PASSWORD)
        viewModel.onConfirmPasswordChanged(VALID_PASSWORD)
        Assert.assertEquals(true, viewModel.registrationScreenState.signUpEnabled)
    }

    @Test
    fun `Test invalid email returns correct error message`() {
        viewModel.onEmailChanged(INVALID_EMAIL)
        viewModel.onSignUpClicked()
        Assert.assertEquals(R.string.email_format, viewModel.registrationScreenState.emailError)
    }

    @Test
    fun `Test email over 100 characters returns correct error message`() {
        viewModel.onEmailChanged(EMAIL_TOO_LONG)
        viewModel.onSignUpClicked()
        Assert.assertEquals(
            R.string.email_length_message,
            viewModel.registrationScreenState.emailError
        )
    }

    @Test
    fun `Test valid email returns null error message`() {
        viewModel.onEmailChanged(VALID_EMAIL)
        viewModel.onSignUpClicked()
        Assert.assertEquals(null, viewModel.registrationScreenState.emailError)
    }

    @Test
    fun `Test invalid passwords return correct error message`() {
        // Too short
        viewModel.onPasswordChanged(PASSWORD_TOO_SHORT)
        viewModel.onSignUpClicked()
        Assert.assertEquals(
            R.string.password_short,
            viewModel.registrationScreenState.passwordError
        )

        // No letters
        viewModel.onPasswordChanged(PASSWORD_NO_LETTERS)
        viewModel.onSignUpClicked()
        Assert.assertEquals(
            R.string.password_missing_letter,
            viewModel.registrationScreenState.passwordError
        )

        // No numbers
        viewModel.onPasswordChanged(PASSWORD_NO_NUMBERS)
        viewModel.onSignUpClicked()
        Assert.assertEquals(
            R.string.password_missing_number,
            viewModel.registrationScreenState.passwordError
        )

        // Length over 50
        viewModel.onPasswordChanged(PASSWORD_TOO_LONG)
        viewModel.onSignUpClicked()
        Assert.assertEquals(
            R.string.password_too_long,
            viewModel.registrationScreenState.passwordError
        )
    }

    @Test
    fun `Test valid password returns null error message`() {
        viewModel.onPasswordChanged(VALID_PASSWORD)
        viewModel.onSignUpClicked()
        Assert.assertEquals(null, viewModel.registrationScreenState.passwordError)
    }

    @Test
    fun `Test non-matching confirm password returns correct error message`() {
        viewModel.onPasswordChanged(VALID_PASSWORD)
        viewModel.onConfirmPasswordChanged(PASSWORD_NO_NUMBERS)
        viewModel.onSignUpClicked()
        Assert.assertEquals(
            R.string.password_match_error,
            viewModel.registrationScreenState.confirmPasswordError
        )
    }

    @Test
    fun `Test matching confirm password returns null error message`() {
        viewModel.onPasswordChanged(VALID_PASSWORD)
        viewModel.onConfirmPasswordChanged(VALID_PASSWORD)
        viewModel.onSignUpClicked()
        Assert.assertEquals(null, viewModel.registrationScreenState.confirmPasswordError)
    }

    @Test
    fun `Test valid new user returns registration success`() {
        viewModel.onEmailChanged(VALID_EMAIL)
        viewModel.onPasswordChanged(VALID_PASSWORD)
        viewModel.onConfirmPasswordChanged(VALID_PASSWORD)
        viewModel.onSignUpClicked()
        Assert.assertEquals(true, viewModel.registrationScreenState.registrationSuccess)
    }

    @Test
    fun `Test registration with existing email returns correct error message`() {
        viewModel.onEmailChanged(COLLISION_EMAIL)
        viewModel.onPasswordChanged(VALID_PASSWORD)
        viewModel.onConfirmPasswordChanged(VALID_PASSWORD)
        viewModel.onSignUpClicked()
        Assert.assertEquals(
            R.string.email_exists,
            viewModel.registrationScreenState.registrationError
        )
    }

    @Test
    fun `Test general registration error returns correct error message`() {
        viewModel.onEmailChanged(ERROR_EMAIL)
        viewModel.onPasswordChanged(VALID_PASSWORD)
        viewModel.onConfirmPasswordChanged(VALID_PASSWORD)
        viewModel.onSignUpClicked()
        Assert.assertEquals(
            R.string.registration_error,
            viewModel.registrationScreenState.registrationError
        )
    }
}