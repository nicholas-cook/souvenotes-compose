package com.souvenotes.souvenotes.settings.reauth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.souvenotes.souvenotes.R
import com.souvenotes.souvenotes.SouvenotesAppBar
import com.souvenotes.souvenotes.SouvenotesScreen
import com.souvenotes.souvenotes.TextFieldError

sealed class ReauthScreenState {
    object Initial : ReauthScreenState()
    object Loading : ReauthScreenState()
    object PasswordLengthError : ReauthScreenState()
    object CredentialsError : ReauthScreenState()
    object ReauthError : ReauthScreenState()
    object ReauthSuccess : ReauthScreenState()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReauthRoute(
    destinationScreen: SouvenotesScreen,
    onReauthSuccess: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: ReauthViewModel = hiltViewModel()
) {
    val reauthScreenState by viewModel.reauthScreenState.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    ReauthScreen(
        destinationScreen = destinationScreen,
        reauthScreenState = reauthScreenState,
        password = password,
        onPasswordChanged = viewModel::onPasswordChanged,
        onErrorDismissed = viewModel::onErrorDismissed,
        onSubmitClicked = viewModel::onSubmitClicked,
        onReauthSuccess = onReauthSuccess,
        onNavigateUp = onNavigateUp
    )
}

@ExperimentalComposeUiApi
@Composable
fun ReauthScreen(
    destinationScreen: SouvenotesScreen,
    reauthScreenState: ReauthScreenState,
    password: String,
    onPasswordChanged: (String) -> Unit,
    onErrorDismissed: () -> Unit,
    onSubmitClicked: () -> Unit,
    onReauthSuccess: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val scaffoldState = rememberScaffoldState()
    if (reauthScreenState == ReauthScreenState.ReauthSuccess) {
        LaunchedEffect(key1 = Unit) {
            onReauthSuccess()
        }
    }
    Scaffold(topBar = {
        ReauthScreenBar(
            destinationScreen = destinationScreen,
            onNavigateUp = onNavigateUp
        )
    }, scaffoldState = scaffoldState) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            if (reauthScreenState == ReauthScreenState.Loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Text(
                text = stringResource(R.string.re_enter_password),
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            val submitEnabled =
                reauthScreenState != ReauthScreenState.Loading && password.isNotEmpty()
            TextField(
                value = password,
                onValueChange = {
                    onPasswordChanged(it)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                label = { Text(text = stringResource(R.string.hint_password)) },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(onGo = {
                    if (submitEnabled) {
                        keyboardController?.hide()
                        onSubmitClicked()
                    }
                }),
                visualTransformation = PasswordVisualTransformation(),
                isError = reauthScreenState == ReauthScreenState.PasswordLengthError
            )
            if (reauthScreenState == ReauthScreenState.PasswordLengthError) {
                TextFieldError(
                    errorRes = R.string.password_too_long,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Button(
                onClick = {
                    keyboardController?.hide()
                    onSubmitClicked()
                },
                enabled = submitEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(text = stringResource(R.string.submit))
            }
        }
        val reauthError = when (reauthScreenState) {
            ReauthScreenState.CredentialsError -> stringResource(id = R.string.error_credentials)
            ReauthScreenState.ReauthError -> stringResource(id = R.string.reauth_error)
            else -> null
        }
        reauthError?.let { errorMessage ->
            LaunchedEffect(key1 = Unit) {
                scaffoldState.snackbarHostState.showSnackbar(message = errorMessage)
                onErrorDismissed()
            }
        }
    }
}

@Composable
fun ReauthScreenBar(destinationScreen: SouvenotesScreen, onNavigateUp: () -> Unit) {
    val title = when (destinationScreen) {
        SouvenotesScreen.ChangeEmail -> R.string.change_email
        SouvenotesScreen.ChangePassword -> R.string.change_password
        SouvenotesScreen.DeleteAccount -> R.string.delete_account
        else -> R.string.title_settings
    }
    SouvenotesAppBar(
        title = title,
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
fun ReauthScreenPreview() {
    ReauthScreen(
        destinationScreen = SouvenotesScreen.ChangeEmail,
        reauthScreenState = ReauthScreenState.Initial,
        password = "",
        onErrorDismissed = {},
        onPasswordChanged = {},
        onSubmitClicked = {},
        onReauthSuccess = {},
        onNavigateUp = {})
}