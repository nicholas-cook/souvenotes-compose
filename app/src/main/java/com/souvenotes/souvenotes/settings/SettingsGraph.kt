package com.souvenotes.souvenotes.settings

import android.os.Build
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.souvenotes.souvenotes.SouvenotesGraph
import com.souvenotes.souvenotes.SouvenotesScreen
import com.souvenotes.souvenotes.policies.PolicyType
import com.souvenotes.souvenotes.settings.delete.DeleteAccountRoute
import com.souvenotes.souvenotes.settings.email.ChangeEmailRoute
import com.souvenotes.souvenotes.settings.password.ChangePasswordRoute
import com.souvenotes.souvenotes.settings.reauth.ReauthRoute

@ExperimentalComposeUiApi
fun NavGraphBuilder.settingsGraph(navHostController: NavHostController) {
    navigation(
        startDestination = SouvenotesScreen.Settings.name,
        route = SouvenotesGraph.UserSettings.name
    ) {
        composable(SouvenotesScreen.Settings.name) {
            SettingsRoute(
                onNavigateUp = { navHostController.navigateUp() },
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
            val destinationScreen = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                entry.arguments?.getSerializable("destinationScreen", SouvenotesScreen::class.java)
            } else {
                entry.arguments?.getSerializable("destinationScreen")
            }
            ReauthRoute(
                destinationScreen = destinationScreen as SouvenotesScreen,
                onReauthSuccess = {
                    navHostController.navigate(destinationScreen.name) {
                        popUpTo(SouvenotesScreen.Settings.name)
                    }
                },
                onNavigateUp = { navHostController.navigateUp() })
        }
        composable(SouvenotesScreen.ChangeEmail.name) {
            ChangeEmailRoute(
                onChangeEmailSuccess = { navHostController.navigateUp() },
                onNavigateUp = { navHostController.navigateUp() })
        }
        composable(SouvenotesScreen.ChangePassword.name) {
            ChangePasswordRoute(
                onChangePasswordSuccess = { navHostController.navigateUp() },
                onNavigateUp = { navHostController.navigateUp() })
        }
        composable(SouvenotesScreen.DeleteAccount.name) {
            DeleteAccountRoute(onDeleteSuccess = {
                navHostController.navigate(SouvenotesGraph.Auth.name) {
                    popUpTo(SouvenotesScreen.NotesList.name) {
                        inclusive = true
                    }
                }
            }, onNavigateUp = { navHostController.navigateUp() })
        }
    }
}