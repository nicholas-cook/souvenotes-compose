package com.souvenotes.souvenotes.registration

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.souvenotes.souvenotes.SouvenotesGraph
import com.souvenotes.souvenotes.SouvenotesScreen
import com.souvenotes.souvenotes.policies.PolicyType
import org.koin.androidx.compose.getViewModel

@ExperimentalComposeUiApi
fun NavGraphBuilder.registrationGraph(navHostController: NavHostController) {
    navigation(
        startDestination = SouvenotesScreen.Registration.name,
        route = SouvenotesGraph.UserRegistration.name
    ) {
        composable(SouvenotesScreen.Registration.name) {
            val registrationViewModel: RegistrationViewModel = getViewModel()
            RegistrationScreen(
                registrationScreenState = registrationViewModel.registrationScreenState,
                onEmailChanged = registrationViewModel::onEmailChanged,
                onPasswordChanged = registrationViewModel::onPasswordChanged,
                onConfirmPasswordChanged = registrationViewModel::onConfirmPasswordChanged,
                onSignUpClicked = registrationViewModel::onSignUpClicked,
                onErrorDismissed = registrationViewModel::onErrorDismissed,
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