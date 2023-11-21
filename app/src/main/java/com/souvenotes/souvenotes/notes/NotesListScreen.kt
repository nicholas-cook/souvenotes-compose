package com.souvenotes.souvenotes.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.souvenotes.repository.model.NotesListItem
import com.souvenotes.souvenotes.*
import com.souvenotes.souvenotes.R
import com.souvenotes.souvenotes.ui.theme.SouvenotesBrown
import com.souvenotes.souvenotes.ui.theme.SouvenotesLightGray
import com.souvenotes.souvenotes.ui.theme.SouvenotesLighterGray
import com.souvenotes.souvenotes.ui.theme.SouvenotesYellow
import org.koin.androidx.compose.getViewModel

data class NotesListScreenState(
    val notes: List<NotesListItem> = listOf(),
    val notesError: Int? = null,
    val toLogin: Boolean = false
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesListRoute(
    onNoteClicked: (String, Long) -> Unit,
    onAddNoteClicked: () -> Unit,
    toLoginScreen: () -> Unit,
    onSettingsClicked: () -> Unit,
    viewModel: NotesListViewModel = getViewModel()
) {
    NotesListScreen(
        notesListScreenState = viewModel.notesListScreenState,
        onNoteClicked = onNoteClicked,
        onAddNoteClicked = onAddNoteClicked,
        onDeleteNote = { noteKey ->
            viewModel.deleteNote(noteKey)
        },
        toLoginScreen = toLoginScreen,
        onErrorDismissed = viewModel::onNotesErrorDismissed,
        onLogoutClicked = viewModel::logout,
        onSettingsClicked = onSettingsClicked
    )
}

@ExperimentalFoundationApi
@Composable
fun NotesListScreen(
    notesListScreenState: NotesListScreenState,
    onNoteClicked: (String, Long) -> Unit,
    onAddNoteClicked: () -> Unit,
    onDeleteNote: (String) -> Unit,
    toLoginScreen: () -> Unit,
    onErrorDismissed: () -> Unit,
    onLogoutClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    var showMaxNotesMessage: Boolean by remember { mutableStateOf(false) }
    var deleteNoteKey: String? by remember { mutableStateOf(null) }
    if (notesListScreenState.toLogin) {
        LaunchedEffect(key1 = notesListScreenState.toLogin) {
            toLoginScreen()
        }
    }
    val context = LocalContext.current
    val adHeight = remember { getAdSize(context).height }
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        topBar = { NotesListScreenBar(onLogoutClicked, onSettingsClicked) },
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (notesListScreenState.notes.size < 100) {
                        onAddNoteClicked()
                    } else {
                        showMaxNotesMessage = true
                    }
                },
                backgroundColor = if (MaterialTheme.colors.isLight) {
                    SouvenotesBrown
                } else {
                    SouvenotesYellow
                },
                modifier = Modifier.padding(
                    bottom = if (notesListScreenState.notes.isEmpty()) {
                        16.dp
                    } else {
                        adHeight.dp
                    }
                )
            ) {
                Icon(Icons.Filled.Add, stringResource(R.string.fab_content_description))
            }
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (notesListScreenState.notes.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(R.string.empty_list),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(
                    items = notesListScreenState.notes,
                    key = { _, note -> note.key }) { index, note ->
                    Column(modifier = Modifier
                        .combinedClickable(
                            onClick = {
                                onNoteClicked(
                                    note.key,
                                    note.createdAt
                                )
                            }, onLongClick = { deleteNoteKey = note.key })
                        .animateItemPlacement()
                    ) {
                        Text(
                            text = if (note.title.isEmpty()) {
                                stringResource(R.string.untitled)
                            } else {
                                note.title
                            },
                            fontSize = 20.sp,
                            modifier = if (index == 0) {
                                Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                            } else {
                                Modifier.padding(start = 16.dp, end = 16.dp)
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = stringResource(R.string.last_updated, note.dateTimeText),
                            fontSize = 16.sp,
                            color = if (MaterialTheme.colors.isLight) {
                                SouvenotesLightGray
                            } else {
                                SouvenotesLighterGray
                            },
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        )
                        Divider()
                    }
                }
            }
            if (notesListScreenState.notes.isNotEmpty()) {
                AndroidView(
                    factory = { context ->
                        getAdView(context, "ca-app-pub-3940256099942544/6300978111")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        notesListScreenState.notesError?.let {
            val errorMessage = stringResource(it)
            LaunchedEffect(key1 = it) {
                scaffoldState.snackbarHostState.showSnackbar(message = errorMessage)
                onErrorDismissed()
            }
        }
        if (showMaxNotesMessage) {
            val errorMessage = stringResource(R.string.max_notes)
            LaunchedEffect(key1 = showMaxNotesMessage) {
                scaffoldState.snackbarHostState.showSnackbar(message = errorMessage)
                showMaxNotesMessage = false
            }
        }
    }
    deleteNoteKey?.let { noteKey ->
        SouvenotesAlertDialog(
            message = R.string.message_delete,
            onDismissRequest = { deleteNoteKey = null },
            confirmText = R.string.option_delete,
            confirmAction = { onDeleteNote(noteKey) },
            cancelText = android.R.string.cancel
        )
    }
}

@Composable
fun NotesListScreenBar(onLogoutClicked: () -> Unit, onSettingsClicked: () -> Unit) {
    var showMenu: Boolean by remember { mutableStateOf(false) }
    var showLogoutDialog: Boolean by remember { mutableStateOf(false) }
    SouvenotesAppBar(
        title = R.string.app_name,
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Filled.MoreVert, stringResource(R.string.content_description_more))
            }
            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                DropdownMenuItem(onClick = {
                    showMenu = false
                    onSettingsClicked()
                }) {
                    Text(text = stringResource(R.string.title_settings))
                }
                DropdownMenuItem(onClick = {
                    showMenu = false
                    showLogoutDialog = true
                }) {
                    Text(text = stringResource(R.string.action_logout))
                }
            }
        })
    if (showLogoutDialog) {
        SouvenotesAlertDialog(
            message = R.string.confirm_logout,
            onDismissRequest = { showLogoutDialog = false },
            confirmText = R.string.action_logout,
            confirmAction = onLogoutClicked,
            cancelText = android.R.string.cancel
        )
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun NotesListScreenPreview() {
    NotesListScreen(
        notesListScreenState = NotesListScreenState(),
        onNoteClicked = { _, _ -> },
        onAddNoteClicked = {},
        onDeleteNote = {},
        toLoginScreen = {},
        onErrorDismissed = {},
        onLogoutClicked = {},
        onSettingsClicked = {})
}