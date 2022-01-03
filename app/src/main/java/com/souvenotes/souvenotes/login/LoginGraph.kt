package com.souvenotes.souvenotes.login

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.souvenotes.souvenotes.SouvenotesGraph
import com.souvenotes.souvenotes.SouvenotesScreen
import com.souvenotes.souvenotes.login.forgotpassword.ForgotPasswordScreen
import com.souvenotes.souvenotes.login.forgotpassword.ForgotPasswordViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalComposeUiApi
fun NavGraphBuilder.loginGraph(navHostController: NavHostController) {
    navigation(
        startDestination = SouvenotesScreen.Login.name,
        route = SouvenotesGraph.Auth.name
    ) {
        composable(SouvenotesScreen.Login.name) {
            val loginViewModel: LoginViewModel = getViewModel()
            LoginScreen(
                loginScreenState = loginViewModel.loginsScreenState,
                onEmailChanged = loginViewModel::onEmailChanged,
                onPasswordChanged = loginViewModel::onPasswordChanged,
                onSubmitClicked = loginViewModel::onSubmitClicked,
                onErrorDismissed = loginViewModel::onLoginErrorDismissed,
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
                }
            )
        }
        composable(SouvenotesScreen.ForgotPassword.name) {
            val forgotPasswordViewModel: ForgotPasswordViewModel = getViewModel()
            ForgotPasswordScreen(
                forgotPasswordScreenState = forgotPasswordViewModel.forgotPasswordScreenState,
                onEmailChanged = forgotPasswordViewModel::onEmailChanged,
                onNavigateUp = { navHostController.navigateUp() },
                onResetClicked = forgotPasswordViewModel::onResetClicked,
                onErrorDismissed = forgotPasswordViewModel::onResetErrorDismissed
            )
        }
    }
}