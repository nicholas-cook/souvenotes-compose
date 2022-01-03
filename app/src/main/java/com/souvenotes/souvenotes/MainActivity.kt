package com.souvenotes.souvenotes

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import com.souvenotes.souvenotes.policies.PolicyScreen
import com.souvenotes.souvenotes.policies.PolicyType
import com.souvenotes.souvenotes.policies.PolicyViewModel
import com.souvenotes.souvenotes.registration.registrationGraph
import com.souvenotes.souvenotes.repository.prefs.AppThemePref
import com.souvenotes.souvenotes.repository.prefs.SouvenotesPrefs
import com.souvenotes.souvenotes.settings.settingsGraph
import com.souvenotes.souvenotes.ui.theme.DarkModeStatus
import com.souvenotes.souvenotes.ui.theme.SouvenotesDarkYellow
import com.souvenotes.souvenotes.ui.theme.SouvenotesTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {

    private val souvenotesPrefs: SouvenotesPrefs by inject()
    private var appTheme by mutableStateOf(souvenotesPrefs.getAppThemePref())
    private val appThemeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == SouvenotesPrefs.KEY_APP_THEME_PREF) {
                appTheme = souvenotesPrefs.getAppThemePref()
            }
        }

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val systemUiController = rememberSystemUiController()
            val isDarkTheme = isSystemInDarkTheme()
            val navHostController = rememberNavController()
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

    override fun onStart() {
        super.onStart()
        souvenotesPrefs.registerOnSharedPreferenceChangeListener(appThemeListener)
    }

    override fun onStop() {
        super.onStop()
        souvenotesPrefs.unregisterOnSharedPreferenceChangeListener(appThemeListener)
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
            val landingViewModel: LandingViewModel = getViewModel()
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
        ) { entry ->
            val policyType = entry.arguments?.getSerializable("policyType")
            val policyViewModel = getViewModel<PolicyViewModel> { parametersOf(policyType) }
            PolicyScreen(
                policyType = policyType as PolicyType,
                policyScreenState = policyViewModel.policyScreenState,
                onErrorDismissed = policyViewModel::onErrorDismissed,
                onNavigateUp = { navHostController.navigateUp() })
        }
        loginGraph(navHostController)
        registrationGraph(navHostController)
        notesGraph(navHostController)
        settingsGraph(navHostController)
    }
}
