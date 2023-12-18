package com.souvenotes.souvenotes.settings.email

import com.souvenotes.souvenotes.COLLISION_EMAIL
import com.souvenotes.souvenotes.EMAIL_TOO_LONG
import com.souvenotes.souvenotes.ERROR_EMAIL
import com.souvenotes.souvenotes.INVALID_EMAIL
import com.souvenotes.souvenotes.VALID_EMAIL
import com.souvenotes.souvenotes.repositories.FakeUserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
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
    fun `Test email is correctly updated`() = runTest {
        val initialState = viewModel.changeEmailScreenState.first()
        Assert.assertEquals(ChangeEmailScreenState.Initial, initialState)

        // Password entered
        viewModel.onEmailChanged(VALID_EMAIL)
        val email = viewModel.email.first()
        Assert.assertEquals(VALID_EMAIL, email)
    }

    @Test
    fun `Test invalid email returns email format error`() = runTest {
        viewModel.onEmailChanged(INVALID_EMAIL)
        viewModel.onSubmitClicked()
        val changeEmailScreenState = viewModel.changeEmailScreenState.first()
        Assert.assertEquals(ChangeEmailScreenState.EmailFormatError, changeEmailScreenState)
    }

    @Test
    fun `Test email over 100 characters returns email length error`() = runTest {
        viewModel.onEmailChanged(EMAIL_TOO_LONG)
        viewModel.onSubmitClicked()
        val changeEmailScreenState = viewModel.changeEmailScreenState.first()
        Assert.assertEquals(ChangeEmailScreenState.EmailLengthError, changeEmailScreenState)
    }

    @Test
    fun `Test valid email returns success`() = runTest {
        viewModel.onEmailChanged(VALID_EMAIL)
        viewModel.onSubmitClicked()
        val changeEmailScreenState = viewModel.changeEmailScreenState.first()
        Assert.assertEquals(ChangeEmailScreenState.Success, changeEmailScreenState)
    }

    @Test
    fun `Test changing to existing email returns email collision error`() = runTest {
        viewModel.onEmailChanged(COLLISION_EMAIL)
        viewModel.onSubmitClicked()
        val changeEmailScreenState = viewModel.changeEmailScreenState.first()
        Assert.assertEquals(ChangeEmailScreenState.EmailCollisionError, changeEmailScreenState)
    }

    @Test
    fun `Test general change email error returns general error`() = runTest {
        viewModel.onEmailChanged(ERROR_EMAIL)
        viewModel.onSubmitClicked()
        val changeEmailScreenState = viewModel.changeEmailScreenState.first()
        Assert.assertEquals(ChangeEmailScreenState.Error, changeEmailScreenState)
    }

    @Test
    fun `Test dismissing the error resets the state`() = runTest {
        viewModel.onEmailChanged(ERROR_EMAIL)
        viewModel.onSubmitClicked()
        var changeEmailScreenState = viewModel.changeEmailScreenState.first()
        Assert.assertEquals(ChangeEmailScreenState.Error, changeEmailScreenState)

        viewModel.onErrorDismissed()
        changeEmailScreenState = viewModel.changeEmailScreenState.first()
        Assert.assertEquals(ChangeEmailScreenState.Initial, changeEmailScreenState)
    }
}