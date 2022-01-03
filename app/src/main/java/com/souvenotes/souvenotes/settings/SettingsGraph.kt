package com.souvenotes.souvenotes.settings

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.souvenotes.souvenotes.SouvenotesGraph
import com.souvenotes.souvenotes.SouvenotesScreen
import com.souvenotes.souvenotes.policies.PolicyType
import com.souvenotes.souvenotes.settings.delete.DeleteAccountScreen
import com.souvenotes.souvenotes.settings.delete.DeleteAccountViewModel
import com.souvenotes.souvenotes.settings.email.ChangeEmailScreen
import com.souvenotes.souvenotes.settings.email.ChangeEmailViewModel
import com.souvenotes.souvenotes.settings.password.ChangePasswordScreen
import com.souvenotes.souvenotes.settings.password.ChangePasswordViewModel
import com.souvenotes.souvenotes.settings.reauth.ReauthScreen
import com.souvenotes.souvenotes.settings.reauth.ReauthViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalComposeUiApi
fun NavGraphBuilder.settingsGraph(navHostController: NavHostController) {
    navigation(
        startDestination = SouvenotesScreen.Settings.name,
        route = SouvenotesGraph.UserSettings.name
    ) {
        composable(SouvenotesScreen.Settings.name) {
            val settingsViewModel: SettingsViewModel = getViewModel()
            SettingsScreen(
                onNavigateUp = { navHostController.navigateUp() },
                currentTheme = settingsViewModel.currentAppTheme,
                onThemeChanged = { settingsViewModel.onAppThemeSelected(it) },
                onChangeEmailClicked = { navHostController.navigate("${SouvenotesScreen.Reauth.name}/${SouvenotesScreen.ChangeEmail}") },
                onChangePasswordClicked = { navHostController.navigate("${SouvenotesScreen.Reauth.name}/${SouvenotesScreen.ChangePassword}") },
                onDeleteAccountClicked = { navHostController.navigate("${SouvenotesScreen.Reauth.name}/${SouvenotesScreen.DeleteAccount}") },
                onTermsClicked = { navHostController.navigate("${SouvenotesScreen.Policy.name}/${PolicyType.Terms}") },
                onPrivacyClicked = { navHostController.navigate("${SouvenotesScreen.Policy.name}/${PolicyType.Privacy}") })
        }
        composable(
            route = "${SouvenotesScreen.Reauth.name}/{destinationScreen}",
            arguments = listOf(
                navArgument("destinationScreen") {
                    type = NavType.EnumType(SouvenotesScreen::class.java)
                })
        ) { entry ->
            val destinationScreen = entry.arguments?.getSerializable("destinationScreen")
            val reauthViewModel: ReauthViewModel = getViewModel()
            ReauthScreen(
                destinationScreen = destinationScreen as SouvenotesScreen,
                reauthScreenState = reauthViewModel.reauthScreenState,
                onPasswordChanged = reauthViewModel::onPasswordChanged,
                onErrorDismissed = reauthViewModel::onErrorDismissed,
                onSubmitClicked = reauthViewModel::onSubmitClicked,
                onReauthSuccess = {
                    navHostController.navigate(destinationScreen.name) {
                        popUpTo(SouvenotesScreen.Settings.name)
                    }
                },
                onNavigateUp = { navHostController.navigateUp() })
        }
        composable(SouvenotesScreen.ChangeEmail.name) {
            val changeEmailViewModel: ChangeEmailViewModel = getViewModel()
            ChangeEmailScreen(
                changeEmailScreenState = changeEmailViewModel.changeEmailScreenState,
                onEmailChanged = changeEmailViewModel::onEmailChanged,
                onSubmitClicked = changeEmailViewModel::onSubmitClicked,
                onErrorDismissed = changeEmailViewModel::onErrorDismissed,
                onChangeEmailSuccess = { navHostController.navigateUp() },
                onNavigateUp = { navHostController.navigateUp() })
        }
        composable(SouvenotesScreen.ChangePassword.name) {
            val changePasswordViewModel: ChangePasswordViewModel = getViewModel()
            ChangePasswordScreen(
                changePasswordScreenState = changePasswordViewModel.changePasswordScreenState,
                onPasswordChanged = changePasswordViewModel::onPasswordChanged,
                onSubmitClicked = changePasswordViewModel::onSubmitClicked,
                onErrorDismissed = changePasswordViewModel::onErrorDismissed,
                onChangePasswordSuccess = { navHostController.navigateUp() },
                onNavigateUp = { navHostController.navigateUp() })
        }
        composable(SouvenotesScreen.DeleteAccount.name) {
            val deleteAccountViewModel: DeleteAccountViewModel = getViewModel()
            DeleteAccountScreen(
                deleteAccountScreenState = deleteAccountViewModel.deleteAccountScreenState,
                onDeleteConfirmed = deleteAccountViewModel::onDeleteConfirmed,
                onDeleteSuccess = {
                    navHostController.navigate(SouvenotesGraph.Auth.name) {
                        popUpTo(SouvenotesScreen.NotesList.name) {
                            inclusive = true
                        }
                    }
                },
                onErrorDismissed = deleteAccountViewModel::onErrorDismissed,
                onNavigateUp = { navHostController.navigateUp() }
            )
        }
    }
}