package com.souvenotes.souvenotes.landing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.souvenotes.souvenotes.R
import com.souvenotes.souvenotes.SouvenotesAppBar
import com.souvenotes.souvenotes.SouvenotesScreen

@Composable
fun LandingScreen(
    destinationScreen: SouvenotesScreen,
    onNavigateToLogin: () -> Unit,
    onNavigateToList: () -> Unit
) {
    Scaffold(topBar = { SouvenotesAppBar(title = R.string.app_name) }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    LaunchedEffect(key1 = destinationScreen) {
        if (destinationScreen == SouvenotesScreen.Login) {
            onNavigateToLogin()
        } else if (destinationScreen == SouvenotesScreen.NotesList) {
            onNavigateToList()
        }
    }
}

@Preview
@Composable
fun LandingScreenPreview() {
    LandingScreen(
        destinationScreen = SouvenotesScreen.Landing,
        onNavigateToLogin = {},
        onNavigateToList = {})
}