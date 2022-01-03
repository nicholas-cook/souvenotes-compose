package com.souvenotes.souvenotes.settings.reauth

import com.souvenotes.souvenotes.*
import com.souvenotes.souvenotes.repositories.FakeUserRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ReauthViewModelTest {

    private val userRepository = FakeUserRepository()

    private lateinit var viewModel: ReauthViewModel

    @Before
    fun setUp() {
        viewModel = ReauthViewModel(userRepository)
    }

    @Test
    fun `Test submit button enabled states`() {
        // Initial state is disabled
        Assert.assertEquals(false, viewModel.reauthScreenState.submitEnabled)

        // Password entered
        viewModel.onPasswordChanged(VALID_PASSWORD)
        Assert.assertEquals(true, viewModel.reauthScreenState.submitEnabled)
    }

    @Test
    fun `Test invalid password returns correct error message`() {
        viewModel.onPasswordChanged(PASSWORD_TOO_SHORT)
        viewModel.onSubmitClicked()
        Assert.assertEquals(R.string.error_credentials, viewModel.reauthScreenState.reauthError)
    }

    @Test
    fun `Test password over 50 characters returns correct error message`() {
        viewModel.onPasswordChanged(PASSWORD_TOO_LONG)
        viewModel.onSubmitClicked()
        Assert.assertEquals(R.string.password_too_long, viewModel.reauthScreenState.passwordError)
    }

    @Test
    fun `Test general reauth error returns correct error message`() {
        viewModel.onPasswordChanged(ERROR_PASSWORD)
        viewModel.onSubmitClicked()
        Assert.assertEquals(R.string.reauth_error, viewModel.reauthScreenState.reauthError)
    }

    @Test
    fun `Test valid password returns reauth success`() {
        viewModel.onPasswordChanged(VALID_PASSWORD)
        viewModel.onSubmitClicked()
        Assert.assertEquals(true, viewModel.reauthScreenState.reauthSuccess)
    }
}