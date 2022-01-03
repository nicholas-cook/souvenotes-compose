package com.souvenotes.souvenotes.settings.password

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.souvenotes.souvenotes.R
import com.souvenotes.souvenotes.SouvenotesAppBar
import com.souvenotes.souvenotes.TextFieldError

data class ChangePasswordScreenState(
    val password: String = "",
    val progressBarVisible: Boolean = false,
    val submitEnabled: Boolean = false,
    val passwordError: Int? = null,
    val changePasswordSuccess: Boolean = false,
    val changePasswordError: Int? = null
)

@ExperimentalComposeUiApi
@Composable
fun ChangePasswordScreen(
    changePasswordScreenState: ChangePasswordScreenState,
    onPasswordChanged: (String) -> Unit,
    onSubmitClicked: () -> Unit,
    onErrorDismissed: () -> Unit,
    onChangePasswordSuccess: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val scaffoldState = rememberScaffoldState()
    if (changePasswordScreenState.changePasswordSuccess) {
        val context = LocalContext.current
        LaunchedEffect(key1 = changePasswordScreenState.changePasswordSuccess) {
            Toast.makeText(context, R.string.change_password_success, Toast.LENGTH_LONG).show()
            onChangePasswordSuccess()
        }
    }
    Scaffold(topBar = { ChangePasswordScreenBar(onNavigateUp = onNavigateUp) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            TextField(
                value = changePasswordScreenState.password,
                onValueChange = {
                    onPasswordChanged(it)
                },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 16.dp)
                    .fillMaxWidth(),
                label = { Text(text = stringResource(R.string.hint_password)) },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                visualTransformation = PasswordVisualTransformation(),
                isError = changePasswordScreenState.passwordError != null
            )
            changePasswordScreenState.passwordError?.let {
                TextFieldError(errorRes = it)
            }
            Text(
                text = stringResource(R.string.password_criteria),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
            )
            Button(
                onClick = {
                    keyboardController?.hide()
                    onSubmitClicked()
                },
                enabled = if (changePasswordScreenState.progressBarVisible) {
                    false
                } else {
                    changePasswordScreenState.submitEnabled
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(text = stringResource(R.string.submit))
            }
        }
        if (changePasswordScreenState.changePasswordError != null) {
            val errorMessage = stringResource(changePasswordScreenState.changePasswordError)
            LaunchedEffect(key1 = changePasswordScreenState.changePasswordError) {
                scaffoldState.snackbarHostState.showSnackbar(message = errorMessage)
                onErrorDismissed()
            }
        }
    }
}

@Composable
fun ChangePasswordScreenBar(onNavigateUp: () -> Unit) {
    SouvenotesAppBar(
        title = R.string.change_password,
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
fun ChangePasswordScreenPreview() {
    ChangePasswordScreen(
        changePasswordScreenState = ChangePasswordScreenState(),
        onPasswordChanged = {},
        onSubmitClicked = {},
        onErrorDismissed = {},
        onChangePasswordSuccess = {},
        onNavigateUp = {})
}