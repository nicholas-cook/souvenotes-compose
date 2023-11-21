package com.souvenotes.souvenotes.login

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.souvenotes.souvenotes.SouvenotesGraph
import com.souvenotes.souvenotes.SouvenotesScreen
import com.souvenotes.souvenotes.login.forgotpassword.ForgotPasswordRoute

@ExperimentalComposeUiApi
fun NavGraphBuilder.loginGraph(navHostController: NavHostController) {
    navigation(
        startDestination = SouvenotesScreen.Login.name,
        route = SouvenotesGraph.Auth.name
    ) {
        composable(SouvenotesScreen.Login.name) {
            LoginRoute(
                onCreateAccountClicked = {
                    navHostController.navigate(SouvenotesGraph.UserRegistration.name)
                },
                onForgotPasswordClicked = {
                    navHostController.navigate(SouvenotesScreen.ForgotPassword.name)
                },
                onLoginSuccess = {
                    navHostController.navigate(SouvenotesScreen.NotesList.name) {
                        popUpTo(SouvenotesScreen.Login.name) {
                            inclusive = true
                        }
                    }
                })
        }
        composable(SouvenotesScreen.ForgotPassword.name) {
            ForgotPasswordRoute(onNavigateUp = { navHostController.navigateUp() })
        }
    }
}