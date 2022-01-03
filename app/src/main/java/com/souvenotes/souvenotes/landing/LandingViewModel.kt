package com.souvenotes.souvenotes.landing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.souvenotes.repository.user.UserRepository
import com.souvenotes.souvenotes.SouvenotesScreen

class LandingViewModel(userRepository: UserRepository) : ViewModel() {

    var destinationScreen by mutableStateOf(SouvenotesScreen.Landing)
        private set

    init {
        if (!userRepository.isUserLoggedIn()) {
            destinationScreen = SouvenotesScreen.Login
        } else {
            userRepository.refreshUser { success ->
                destinationScreen = if (success) {
                    SouvenotesScreen.NotesList
                } else {
                    SouvenotesScreen.Login
                }
            }
        }
    }
}