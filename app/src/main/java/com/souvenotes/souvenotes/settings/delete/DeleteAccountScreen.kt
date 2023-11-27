package com.souvenotes.souvenotes.settings.delete

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.souvenotes.souvenotes.R
import com.souvenotes.souvenotes.SouvenotesAlertDialog
import com.souvenotes.souvenotes.SouvenotesAppBar

data class DeleteAccountScreenState(
    val progressBarVisible: Boolean = false,
    val deleteSuccess: Boolean = false,
    val deleteError: Boolean = false
)

@Composable
fun DeleteAccountRoute(
    onDeleteSuccess: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: DeleteAccountViewModel = hiltViewModel()
) {
    DeleteAccountScreen(
        deleteAccountScreenState = viewModel.deleteAccountScreenState,
        onDeleteConfirmed = viewModel::onDeleteConfirmed,
        onDeleteSuccess = onDeleteSuccess,
        onErrorDismissed = viewModel::onErrorDismissed,
        onNavigateUp = onNavigateUp
    )

}

@Composable
fun DeleteAccountScreen(
    deleteAccountScreenState: DeleteAccountScreenState,
    onDeleteConfirmed: () -> Unit,
    onDeleteSuccess: () -> Unit,
    onErrorDismissed: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()
    var showConfirmationDialog: Boolean by remember { mutableStateOf(false) }
    if (deleteAccountScreenState.deleteSuccess) {
        LaunchedEffect(key1 = deleteAccountScreenState.deleteSuccess) {
            onDeleteSuccess()
        }
    }
    Scaffold(
        topBar = { DeleteAccountScreenBar(onNavigateUp = onNavigateUp) },
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Text(
                text = stringResource(R.string.delete_message),
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = { showConfirmationDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(text = stringResource(R.string.delete_account))
            }
        }
        if (deleteAccountScreenState.deleteError) {
            val errorMessage = stringResource(R.string.delete_account_error)
            LaunchedEffect(key1 = deleteAccountScreenState.deleteError) {
                scaffoldState.snackbarHostState.showSnackbar(message = errorMessage)
                onErrorDismissed()
            }
        }
        if (showConfirmationDialog) {
            SouvenotesAlertDialog(
                message = R.string.confirm_delete,
                onDismissRequest = { showConfirmationDialog = false },
                confirmText = R.string.option_delete,
                confirmAction = onDeleteConfirmed,
                cancelText = android.R.string.cancel
            )
        }
    }
}

@Composable
fun DeleteAccountScreenBar(onNavigateUp: () -> Unit) {
    SouvenotesAppBar(
        title = R.string.delete_account,
        navigationIcon = {
            IconButton(onClick = { onNavigateUp() }) {
                Icon(Icons.Filled.ArrowBack, stringResource(R.string.content_description_back))
            }
        }
    )
}

@Preview
@Composable
fun DeleteAccountScreenPreview() {
    DeleteAccountScreen(
        deleteAccountScreenState = DeleteAccountScreenState(),
        onDeleteConfirmed = {},
        onDeleteSuccess = {},
        onErrorDismissed = {},
        onNavigateUp = {})
}