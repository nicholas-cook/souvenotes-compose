package com.souvenotes.souvenotes.notes.edit

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.souvenotes.souvenotes.R
import com.souvenotes.souvenotes.SouvenotesAlertDialog
import com.souvenotes.souvenotes.SouvenotesAppBar
import com.souvenotes.souvenotes.TextFieldError

data class EditNoteScreenState(
    val title: String = "",
    val content: String = "",
    val isNewNote: Boolean = false,
    val loadNoteError: Boolean = false,
    val deleteNoteError: Boolean = false,
    val titleLengthError: Boolean = false,
    val contentLengthError: Boolean = false,
    val toList: Boolean = false
)

@ExperimentalComposeUiApi
@Composable
fun EditNoteScreen(
    editNoteScreenState: EditNoteScreenState,
    onTitleChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit,
    onDeleteClicked: () -> Unit,
    onErrorDismissed: () -> Unit,
    onScreenExit: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                onScreenExit()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    if (editNoteScreenState.loadNoteError) {
        Toast.makeText(LocalContext.current, R.string.note_load_error, Toast.LENGTH_LONG).show()
    }
    if (editNoteScreenState.toList) {
        LaunchedEffect(key1 = editNoteScreenState.toList) {
            onNavigateUp()
        }
    }
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        topBar = {
            EditNoteScreenBar(
                isNewNote = editNoteScreenState.isNewNote,
                onNavigateUp = onNavigateUp,
                onDeleteClicked = onDeleteClicked
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TextField(
                value = editNoteScreenState.title,
                onValueChange = {
                    onTitleChanged(it)
                },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                label = {
                    if (editNoteScreenState.title.isEmpty()) {
                        Text(text = stringResource(R.string.hint_title))
                    }
                },
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                isError = editNoteScreenState.titleLengthError
            )
            if (editNoteScreenState.titleLengthError) {
                TextFieldError(errorRes = R.string.title_too_long)
            }
            TextField(
                value = editNoteScreenState.content,
                onValueChange = {
                    onContentChanged(it)
                },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
                    .fillMaxWidth()
                    .weight(1f),
                label = {
                    if (editNoteScreenState.content.isEmpty()) {
                        Text(text = stringResource(R.string.hint_content))
                    }
                },
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                isError = editNoteScreenState.contentLengthError
            )
            if (editNoteScreenState.contentLengthError) {
                TextFieldError(
                    errorRes = R.string.content_too_long,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
        if (editNoteScreenState.deleteNoteError) {
            val errorMessage = stringResource(R.string.delete_error)
            LaunchedEffect(key1 = editNoteScreenState.deleteNoteError) {
                scaffoldState.snackbarHostState.showSnackbar(message = errorMessage)
                onErrorDismissed()
            }
        }
    }
}

@Composable
fun EditNoteScreenBar(isNewNote: Boolean, onNavigateUp: () -> Unit, onDeleteClicked: () -> Unit) {
    var showDeleteDialog: Boolean by remember { mutableStateOf(false) }
    val title = if (isNewNote) {
        R.string.title_add_note
    } else {
        R.string.title_edit_note
    }
    SouvenotesAppBar(
        title = title,
        navigationIcon = {
            IconButton(onClick = { onNavigateUp() }) {
                Icon(Icons.Filled.ArrowBack, stringResource(R.string.content_description_back))
            }
        },
        actions = {
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Filled.Delete, stringResource(R.string.option_delete))
            }
        }
    )
    if (showDeleteDialog) {
        SouvenotesAlertDialog(
            message = R.string.message_delete,
            onDismissRequest = { showDeleteDialog = false },
            confirmText = R.string.option_delete,
            confirmAction = onDeleteClicked,
            cancelText = android.R.string.cancel
        )
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun EditNoteScreenPreview() {
    EditNoteScreen(
        editNoteScreenState = EditNoteScreenState(),
        onTitleChanged = {},
        onContentChanged = {},
        onDeleteClicked = {},
        onErrorDismissed = {},
        onScreenExit = {},
        onNavigateUp = {}
    )
}