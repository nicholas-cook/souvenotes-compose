package com.souvenotes.souvenotes.landing

import com.souvenotes.souvenotes.SouvenotesScreen
import com.souvenotes.souvenotes.VALID_EMAIL
import com.souvenotes.souvenotes.VALID_PASSWORD
import com.souvenotes.souvenotes.repositories.FakeUserRepository
import org.junit.Assert
import org.junit.Test

class LandingViewModelTest {

    private val userRepository = FakeUserRepository()

    private lateinit var viewModel: LandingViewModel

    private fun init(logout: Boolean = false, refreshError: Boolean = false) {
        if (logout) {
            userRepository.logout()
        } else {
            userRepository.login(VALID_EMAIL, VALID_PASSWORD) {}
            userRepository.refreshUserError = refreshError
        }
        viewModel = LandingViewModel(userRepository)
    }

    @Test
    fun `Test logged out user redirected to login screen`() {
        init(logout = true)
        Assert.assertEquals(SouvenotesScreen.Login, viewModel.destinationScreen)
    }

    @Test
    fun `Test refresh user error redirects to login screen`() {
        init(refreshError = true)
        Assert.assertEquals(SouvenotesScreen.Login, viewModel.destinationScreen)
    }

    @Test
    fun `Test refresh user success redirects to notes list screen`() {
        init(refreshError = false)
        Assert.assertEquals(SouvenotesScreen.NotesList, viewModel.destinationScreen)
    }
}