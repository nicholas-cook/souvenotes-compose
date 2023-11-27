package com.souvenotes.souvenotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.souvenotes.souvenotes.landing.LandingScreen
import com.souvenotes.souvenotes.landing.LandingViewModel
import com.souvenotes.souvenotes.login.loginGraph
import com.souvenotes.souvenotes.notes.notesGraph
import com.souvenotes.souvenotes.policies.PolicyRoute
import com.souvenotes.souvenotes.policies.PolicyType
import com.souvenotes.souvenotes.registration.registrationGraph
import com.souvenotes.souvenotes.repository.prefs.AppThemePref
import com.souvenotes.souvenotes.settings.settingsGraph
import com.souvenotes.souvenotes.ui.theme.DarkModeStatus
import com.souvenotes.souvenotes.ui.theme.SouvenotesDarkYellow
import com.souvenotes.souvenotes.ui.theme.SouvenotesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val systemUiController = rememberSystemUiController()
            val isDarkTheme = isSystemInDarkTheme()
            val navHostController = rememberNavController()
            val appViewModel: AppViewModel = hiltViewModel()
            val appTheme by appViewModel.appTheme.collectAsStateWithLifecycle()
            SideEffect {
                val statusBarColor = when (appTheme) {
                    AppThemePref.Light -> SouvenotesDarkYellow
                    AppThemePref.Dark -> DarkModeStatus
                    AppThemePref.System -> if (isDarkTheme) {
                        DarkModeStatus
                    } else {
                        SouvenotesDarkYellow
                    }
                }
                systemUiController.setStatusBarColor(color = statusBarColor)
            }
            SouvenotesTheme(appTheme) {
                Surface(color = MaterialTheme.colors.surface) {
                    SouvenotesNavHost(navHostController = navHostController)
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun SouvenotesNavHost(navHostController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navHostController,
        startDestination = SouvenotesScreen.Landing.name,
        modifier = modifier
    ) {
        composable(SouvenotesScreen.Landing.name) {
            val landingViewModel: LandingViewModel = hiltViewModel()
            LandingScreen(
                destinationScreen = landingViewModel.destinationScreen,
                onNavigateToLogin = {
                    navHostController.navigate(SouvenotesGraph.Auth.name) {
                        popUpTo(SouvenotesScreen.Landing.name) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToList = {
                    navHostController.navigate(SouvenotesGraph.Notes.name) {
                        popUpTo(SouvenotesScreen.Landing.name) {
                            inclusive = true
                        }
                    }
                })
        }
        composable(
            route = "${SouvenotesScreen.Policy.name}/{policyType}",
            arguments = listOf(
                navArgument("policyType") {
                    type = NavType.EnumType(PolicyType::class.java)
                })
        ) {
            PolicyRoute(onNavigateUp = { navHostController.navigateUp() })
        }
        loginGraph(navHostController)
        registrationGraph(navHostController)
        notesGraph(navHostController)
        settingsGraph(navHostController)
    }
}
