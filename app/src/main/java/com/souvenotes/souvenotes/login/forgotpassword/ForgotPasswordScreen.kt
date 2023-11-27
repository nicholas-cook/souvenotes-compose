package com.souvenotes.souvenotes.login.forgotpassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.souvenotes.souvenotes.R
import com.souvenotes.souvenotes.SouvenotesSnackbarHost
import com.souvenotes.souvenotes.TextFieldError
import com.souvenotes.souvenotes.ui.theme.SouvenotesBrown
import com.souvenotes.souvenotes.ui.theme.SouvenotesYellow

data class ForgotPasswordScreenState(
    val email: String = "",
    val resetEnabled: Boolean = false,
    val progressBarVisible: Boolean = false,
    val emailError: Int? = null,
    val resetError: Int? = null,
    val resetSuccess: Boolean = false
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ForgotPasswordRoute(
    onNavigateUp: () -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    ForgotPasswordScreen(
        forgotPasswordScreenState = viewModel.forgotPasswordScreenState,
        onEmailChanged = viewModel::onEmailChanged,
        onNavigateUp = onNavigateUp,
        onResetClicked = viewModel::onResetClicked,
        onErrorDismissed = viewModel::onResetErrorDismissed
    )
}

@ExperimentalComposeUiApi
@Composable
fun ForgotPasswordScreen(
    forgotPasswordScreenState: ForgotPasswordScreenState,
    onEmailChanged: (String?) -> Unit,
    onNavigateUp: () -> Unit,
    onResetClicked: () -> Unit,
    onErrorDismissed: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        topBar = { ForgotPasswordScreenBar(onNavigateUp) },
        scaffoldState = scaffoldState,
        snackbarHost = { SouvenotesSnackbarHost(hostState = it) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            if (forgotPasswordScreenState.progressBarVisible) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            TextField(
                value = forgotPasswordScreenState.email,
                onValueChange = {
                    onEmailChanged(it)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                label = { Text(text = stringResource(R.string.hint_email)) },
                maxLines = 1,
                singleLine = true,
                isError = forgotPasswordScreenState.emailError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            if (forgotPasswordScreenState.emailError != null) {
                TextFieldError(errorRes = forgotPasswordScreenState.emailError)
            }
            Button(
                onClick = {
                    keyboardController?.hide()
                    onResetClicked()
                },
                enabled = forgotPasswordScreenState.resetEnabled,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.action_reset))
            }
        }
        if (forgotPasswordScreenState.resetError != null) {
            val errorMessage = stringResource(forgotPasswordScreenState.resetError)
            LaunchedEffect(key1 = forgotPasswordScreenState.resetError) {
                scaffoldState.snackbarHostState.showSnackbar(message = errorMessage)
                onErrorDismissed()
            }
        }
        if (forgotPasswordScreenState.resetSuccess) {
            val successMessage = stringResource(R.string.reset_email_sent)
            val successAction = stringResource(android.R.string.ok)
            LaunchedEffect(key1 = successMessage) {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = successMessage,
                    actionLabel = successAction,
                    duration = SnackbarDuration.Indefinite
                )
                when (result) {
                    SnackbarResult.Dismissed -> onNavigateUp()
                    SnackbarResult.ActionPerformed -> onNavigateUp()
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordScreenBar(onNavigateUp: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(R.string.title_reset_password)) },
        backgroundColor = SouvenotesYellow,
        contentColor = SouvenotesBrown,
        navigationIcon = {
            IconButton(onClick = { onNavigateUp() }) {
                Icon(Icons.Filled.ArrowBack, stringResource(R.string.content_description_back))
            }
        }
    )
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(
        forgotPasswordScreenState = ForgotPasswordScreenState(),
        onEmailChanged = {},
        onNavigateUp = {},
        onResetClicked = {},
        onErrorDismissed = {}
    )
}