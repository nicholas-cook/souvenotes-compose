package com.souvenotes.souvenotes.settings.email

import android.widget.Toast
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.souvenotes.souvenotes.R
import com.souvenotes.souvenotes.SouvenotesAppBar
import com.souvenotes.souvenotes.TextFieldError

data class ChangeEmailScreenState(
    val email: String = "",
    val progressBarVisible: Boolean = false,
    val submitEnabled: Boolean = false,
    val emailError: Int? = null,
    val changeEmailSuccess: Boolean = false,
    val changeEmailError: Int? = null
)

@ExperimentalComposeUiApi
@Composable
fun ChangeEmailScreen(
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
    if (changeEmailScreenState.changeEmailSuccess) {
        val context = LocalContext.current
        LaunchedEffect(key1 = changeEmailScreenState.changeEmailSuccess) {
            Toast.makeText(context, R.string.email_change_success, Toast.LENGTH_LONG).show()
            onChangeEmailSuccess()
        }
    }
    Scaffold(topBar = { ChangeEmailScreenBar(onNavigateUp) }, scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            if (changeEmailScreenState.progressBarVisible) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            TextField(
                value = changeEmailScreenState.email,
                onValueChange = {
                    onEmailChanged(it)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                label = { Text(text = stringResource(R.string.hint_change_email)) },
                maxLines = 1,
                singleLine = true,
                isError = changeEmailScreenState.emailError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
            if (changeEmailScreenState.emailError != null) {
                TextFieldError(
                    errorRes = changeEmailScreenState.emailError,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Button(
                onClick = {
                    keyboardController?.hide()
                    onSubmitClicked()
                },
                enabled = if (changeEmailScreenState.progressBarVisible) {
                    false
                } else {
                    changeEmailScreenState.submitEnabled
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(text = stringResource(R.string.submit))
            }
            if (changeEmailScreenState.changeEmailError != null) {
                val errorMessage = stringResource(changeEmailScreenState.changeEmailError)
                LaunchedEffect(key1 = changeEmailScreenState.changeEmailError) {
                    scaffoldState.snackbarHostState.showSnackbar(message = errorMessage)
                    onErrorDismissed()
                }
            }
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
        changeEmailScreenState = ChangeEmailScreenState(),
        onEmailChanged = {},
        onSubmitClicked = {},
        onErrorDismissed = {},
        onChangeEmailSuccess = {},
        onNavigateUp = {})
}