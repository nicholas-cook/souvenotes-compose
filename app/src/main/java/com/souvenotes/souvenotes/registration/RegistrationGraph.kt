package com.souvenotes.souvenotes.registration

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.souvenotes.souvenotes.SouvenotesGraph
import com.souvenotes.souvenotes.SouvenotesScreen
import com.souvenotes.souvenotes.policies.PolicyType

@ExperimentalComposeUiApi
fun NavGraphBuilder.registrationGraph(navHostController: NavHostController) {
    navigation(
        startDestination = SouvenotesScreen.Registration.name,
        route = SouvenotesGraph.UserRegistration.name
    ) {
        composable(SouvenotesScreen.Registration.name) {
            RegistrationRoute(
                onTermsClicked = { navHostController.navigate("${SouvenotesScreen.Policy.name}/${PolicyType.Terms}") },
                onPrivacyClicked = { navHostController.navigate("${SouvenotesScreen.Policy.name}/${PolicyType.Privacy}") },
                onRegistrationSuccess = {
                    navHostController.navigate(SouvenotesScreen.NotesList.name) {
                        popUpTo(SouvenotesScreen.Login.name) {
                            inclusive = true
                        }
                    }
                },
                onNavigateUp = { navHostController.navigateUp() })
        }
    }
}