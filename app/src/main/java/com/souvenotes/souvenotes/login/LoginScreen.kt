package com.souvenotes.souvenotes.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.souvenotes.souvenotes.R
import com.souvenotes.souvenotes.SouvenotesAppBar
import com.souvenotes.souvenotes.TextFieldError
import org.koin.androidx.compose.getViewModel

data class LoginScreenState(
    val email: String = "",
    val password: String = "",
    val submitEnabled: Boolean = false,
    val progressBarVisible: Boolean = false,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val loginError: Int? = null,
    val loginSuccess: Boolean = false
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginRoute(
    onCreateAccountClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = getViewModel()
) {
    val loginScreenState = viewModel.loginsScreenState
    LoginScreen(
        loginScreenState = loginScreenState,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onSubmitClicked = viewModel::onSubmitClicked,
        onErrorDismissed = viewModel::onLoginErrorDismissed,
        onCreateAccountClicked = onCreateAccountClicked,
        onForgotPasswordClicked = onForgotPasswordClicked,
        onLoginSuccess = onLoginSuccess
    )
}

@ExperimentalComposeUiApi
@Composable
fun LoginScreen(
    loginScreenState: LoginScreenState,
    onEmailChanged: (String?) -> Unit,
    onPasswordChanged: (String?) -> Unit,
    onSubmitClicked: () -> Unit,
    onErrorDismissed: () -> Unit,
    onCreateAccountClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    if (loginScreenState.loginSuccess) {
        LaunchedEffect(key1 = loginScreenState.loginSuccess) {
            onLoginSuccess()
        }
    }
    val scaffoldState = rememberScaffoldState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = { SouvenotesAppBar(title = R.string.title_login) },
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            if (loginScreenState.progressBarVisible) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            TextField(
                value = loginScreenState.email,
                onValueChange = {
                    onEmailChanged(it)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                label = { Text(text = stringResource(R.string.hint_email)) },
                maxLines = 1,
                singleLine = true,
                isError = loginScreenState.emailError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            if (loginScreenState.emailError != null) {
                TextFieldError(errorRes = loginScreenState.emailError)
            }
            TextField(
                value = loginScreenState.password,
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
                    if (loginScreenState.submitEnabled) {
                        keyboardController?.hide()
                        onSubmitClicked()
                    }
                }),
                visualTransformation = PasswordVisualTransformation(),
                isError = loginScreenState.passwordError != null
            )
            if (loginScreenState.passwordError != null) {
                TextFieldError(errorRes = loginScreenState.passwordError)
            }
            Button(
                onClick = {
                    keyboardController?.hide()
                    onSubmitClicked()
                },
                enabled = if (loginScreenState.progressBarVisible) {
                    false
                } else {
                    loginScreenState.submitEnabled
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(R.string.submit))
            }
            TextButton(
                onClick = { onCreateAccountClicked() },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(R.string.create_account))
            }
            TextButton(
                onClick = { onForgotPasswordClicked() },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(R.string.forgot_password))
            }
        }
        if (loginScreenState.loginError != null) {
            val errorMessage = stringResource(loginScreenState.loginError)
            LaunchedEffect(key1 = loginScreenState.loginError) {
                scaffoldState.snackbarHostState.showSnackbar(message = errorMessage)
                onErrorDismissed()
            }
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        loginScreenState = LoginScreenState(),
        onEmailChanged = {},
        onPasswordChanged = {},
        onSubmitClicked = {},
        onErrorDismissed = {},
        onCreateAccountClicked = {},
        onForgotPasswordClicked = {},
        onLoginSuccess = {}
    )
}