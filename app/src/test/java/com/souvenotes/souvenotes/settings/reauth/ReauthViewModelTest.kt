package com.souvenotes.souvenotes.settings.reauth

import com.souvenotes.souvenotes.ERROR_PASSWORD
import com.souvenotes.souvenotes.PASSWORD_TOO_LONG
import com.souvenotes.souvenotes.PASSWORD_TOO_SHORT
import com.souvenotes.souvenotes.VALID_PASSWORD
import com.souvenotes.souvenotes.repositories.FakeUserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
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
    fun `Test password is correctly updated`() = runTest {
        val initialState = viewModel.reauthScreenState.first()
        Assert.assertEquals(ReauthScreenState.Initial, initialState)

        // Password entered
        viewModel.onPasswordChanged(VALID_PASSWORD)
        val password = viewModel.password.first()
        Assert.assertEquals(VALID_PASSWORD, password)
    }

    @Test
    fun `Test invalid password returns correct error message`() = runTest {
        viewModel.onPasswordChanged(PASSWORD_TOO_SHORT)
        viewModel.onSubmitClicked()
        val reauthScreenState = viewModel.reauthScreenState.first()
        Assert.assertEquals(ReauthScreenState.CredentialsError, reauthScreenState)
    }

    @Test
    fun `Test password over 50 characters returns correct error message`() = runTest {
        viewModel.onPasswordChanged(PASSWORD_TOO_LONG)
        viewModel.onSubmitClicked()
        val reauthScreenState = viewModel.reauthScreenState.first()
        Assert.assertEquals(ReauthScreenState.PasswordLengthError, reauthScreenState)
    }

    @Test
    fun `Test general reauth error returns correct error message`() = runTest {
        viewModel.onPasswordChanged(ERROR_PASSWORD)
        viewModel.onSubmitClicked()
        val reauthScreenState = viewModel.reauthScreenState.first()
        Assert.assertEquals(ReauthScreenState.ReauthError, reauthScreenState)
    }

    @Test
    fun `Test valid password returns reauth success`() = runTest {
        viewModel.onPasswordChanged(VALID_PASSWORD)
        viewModel.onSubmitClicked()
        val reauthScreenState = viewModel.reauthScreenState.first()
        Assert.assertEquals(ReauthScreenState.ReauthSuccess, reauthScreenState)
    }

    @Test
    fun `Test dismissing the error resets the state`() = runTest {
        viewModel.onPasswordChanged(ERROR_PASSWORD)
        viewModel.onSubmitClicked()
        var reauthScreenState = viewModel.reauthScreenState.first()
        Assert.assertEquals(ReauthScreenState.ReauthError, reauthScreenState)

        viewModel.onErrorDismissed()
        reauthScreenState = viewModel.reauthScreenState.first()
        Assert.assertEquals(ReauthScreenState.Initial, reauthScreenState)
    }
}