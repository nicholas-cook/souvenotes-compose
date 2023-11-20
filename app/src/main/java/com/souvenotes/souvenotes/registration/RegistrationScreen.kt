package com.souvenotes.souvenotes.registration

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

data class RegistrationScreenState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val progressBarVisible: Boolean = false,
    val signUpEnabled: Boolean = false,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val registrationError: Int? = null,
    val registrationSuccess: Boolean = false
)

@ExperimentalComposeUiApi
@Composable
fun RegistrationScreen(
    registrationScreenState: RegistrationScreenState,
    onEmailChanged: (String?) -> Unit,
    onPasswordChanged: (String?) -> Unit,
    onConfirmPasswordChanged: (String?) -> Unit,
    onSignUpClicked: () -> Unit,
    onErrorDismissed: () -> Unit,
    onTermsClicked: () -> Unit,
    onPrivacyClicked: () -> Unit,
    onRegistrationSuccess: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = { RegistrationScreenBar(onNavigateUp) },
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            if (registrationScreenState.progressBarVisible) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            TextField(
                value = registrationScreenState.email,
                onValueChange = {
                    onEmailChanged(it)
                },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                label = { Text(text = stringResource(R.string.hint_email)) },
                maxLines = 1,
                singleLine = true,
                isError = registrationScreenState.emailError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            registrationScreenState.emailError?.let {
                TextFieldError(errorRes = it)
            }
            TextField(
                value = registrationScreenState.password,
                onValueChange = {
                    onPasswordChanged(it)
                },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
                    .fillMaxWidth(),
                label = { Text(text = stringResource(R.string.hint_password)) },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = PasswordVisualTransformation(),
                isError = registrationScreenState.passwordError != null
            )
            registrationScreenState.passwordError?.let {
                TextFieldError(errorRes = it)
            }
            Text(
                text = stringResource(R.string.password_criteria),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
            )
            TextField(
                value = registrationScreenState.confirmPassword,
                onValueChange = {
                    onConfirmPasswordChanged(it)
                },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
                    .fillMaxWidth(),
                label = { Text(text = stringResource(R.string.confirm_password)) },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                visualTransformation = PasswordVisualTransformation(),
                isError = registrationScreenState.confirmPasswordError != null
            )
            registrationScreenState.confirmPasswordError?.let {
                TextFieldError(errorRes = it)
            }
            Text(
                text = stringResource(R.string.policy_agreement),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
            )
            TextButton(
                onClick = { onTermsClicked() },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(R.string.terms_and_conditions))
            }
            Text(
                text = stringResource(R.string.and),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            TextButton(
                onClick = { onPrivacyClicked() },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(R.string.privacy_policy))
            }
            Button(
                onClick = {
                    keyboardController?.hide()
                    onSignUpClicked()
                },
                enabled = if (registrationScreenState.progressBarVisible) {
                    false
                } else {
                    registrationScreenState.signUpEnabled
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.button_sign_up))
            }
        }
        registrationScreenState.registrationError?.let {
            val errorMessage = stringResource(it)
            LaunchedEffect(key1 = it) {
                scaffoldState.snackbarHostState.showSnackbar(message = errorMessage)
                onErrorDismissed()
            }
        }
        if (registrationScreenState.registrationSuccess) {
            LaunchedEffect(key1 = registrationScreenState.registrationSuccess) {
                onRegistrationSuccess()
            }
        }
    }
}

@Composable
fun RegistrationScreenBar(onNavigateUp: () -> Unit) {
    SouvenotesAppBar(
        title = R.string.title_registration,
        navigationIcon = {
            IconButton(onClick = { onNavigateUp() }) {
                Icon(Icons.Filled.ArrowBack, stringResource(R.string.content_description_back))
            }
        })
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen(
        registrationScreenState = RegistrationScreenState(),
        onEmailChanged = {},
        onPasswordChanged = {},
        onConfirmPasswordChanged = {},
        onSignUpClicked = {},
        onErrorDismissed = {},
        onTermsClicked = {},
        onPrivacyClicked = {},
        onRegistrationSuccess = {},
        onNavigateUp = {})
}