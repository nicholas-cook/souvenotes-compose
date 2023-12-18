package com.souvenotes.souvenotes.settings.email

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.souvenotes.souvenotes.R
import com.souvenotes.souvenotes.SouvenotesAlertDialog
import com.souvenotes.souvenotes.SouvenotesAppBar
import com.souvenotes.souvenotes.TextFieldError

sealed class ChangeEmailScreenState {
    object Initial : ChangeEmailScreenState()
    object Loading : ChangeEmailScreenState()
    object EmailFormatError : ChangeEmailScreenState()
    object EmailLengthError : ChangeEmailScreenState()
    object EmailCollisionError : ChangeEmailScreenState()
    object Error : ChangeEmailScreenState()
    object Success : ChangeEmailScreenState()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChangeEmailRoute(
    onChangeEmailSuccess: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: ChangeEmailViewModel = hiltViewModel()
) {
    val changeEmailScreenState by viewModel.changeEmailScreenState.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    ChangeEmailScreen(
        email = email,
        changeEmailScreenState = changeEmailScreenState,
        onEmailChanged = viewModel::onEmailChanged,
        onSubmitClicked = viewModel::onSubmitClicked,
        onErrorDismissed = viewModel::onErrorDismissed,
        onChangeEmailSuccess = onChangeEmailSuccess,
        onNavigateUp = onNavigateUp
    )
}

@ExperimentalComposeUiApi
@Composable
fun ChangeEmailScreen(
    email: String,
    changeEmailScreenState: ChangeEmailScreenState,
    onEmailChanged: (String) -> Unit,
    onSubmitClicked: () -> Unit,
    onErrorDismissed: () -> Unit,
    onChangeEmailSuccess: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        topBar = { ChangeEmailScreenBar(onNavigateUp) },
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            if (changeEmailScreenState == ChangeEmailScreenState.Loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            TextField(
                value = email,
                onValueChange = {
                    onEmailChanged(it)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                label = { Text(text = stringResource(R.string.hint_change_email)) },
                maxLines = 1,
                singleLine = true,
                isError = changeEmailScreenState == ChangeEmailScreenState.EmailFormatError ||
                        changeEmailScreenState == ChangeEmailScreenState.EmailLengthError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
            when (changeEmailScreenState) {
                ChangeEmailScreenState.EmailFormatError -> TextFieldError(
                    errorRes = R.string.email_format,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ChangeEmailScreenState.EmailLengthError -> TextFieldError(
                    errorRes = R.string.email_length_message,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                else -> {}
            }
            Button(
                onClick = {
                    keyboardController?.hide()
                    onSubmitClicked()
                },
                enabled = changeEmailScreenState != ChangeEmailScreenState.Loading &&
                        email.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(text = stringResource(R.string.submit))
            }
            when (changeEmailScreenState) {
                ChangeEmailScreenState.EmailCollisionError -> {
                    val errorMessage = stringResource(R.string.email_exists)
                    LaunchedEffect(key1 = Unit) {
                        scaffoldState.snackbarHostState.showSnackbar(message = errorMessage)
                        onErrorDismissed()
                    }
                }

                ChangeEmailScreenState.Error -> {
                    val errorMessage = stringResource(R.string.change_email_error)
                    LaunchedEffect(key1 = Unit) {
                        scaffoldState.snackbarHostState.showSnackbar(message = errorMessage)
                        onErrorDismissed()
                    }
                }

                else -> {}
            }
        }
        if (changeEmailScreenState == ChangeEmailScreenState.Success) {
            SouvenotesAlertDialog(
                message = R.string.email_change_success,
                onDismissRequest = onChangeEmailSuccess,
                confirmText = android.R.string.ok,
                confirmAction = onChangeEmailSuccess
            )
        }
    }
}

@Composable
fun ChangeEmailScreenBar(onNavigateUp: () -> Unit) {
    SouvenotesAppBar(
        title = R.string.change_email,
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
fun ChangeEmailScreenPreview() {
    ChangeEmailScreen(
        email = "",
        changeEmailScreenState = ChangeEmailScreenState.Initial,
        onEmailChanged = {},
        onSubmitClicked = {},
        onErrorDismissed = {},
        onChangeEmailSuccess = {},
        onNavigateUp = {})
}