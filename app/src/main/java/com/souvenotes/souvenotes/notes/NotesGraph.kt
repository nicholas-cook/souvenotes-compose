package com.souvenotes.souvenotes.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.souvenotes.souvenotes.SouvenotesGraph
import com.souvenotes.souvenotes.SouvenotesScreen
import com.souvenotes.souvenotes.notes.edit.EditNoteRoute

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
fun NavGraphBuilder.notesGraph(navHostController: NavHostController) {
    navigation(
        startDestination = SouvenotesScreen.NotesList.name,
        route = SouvenotesGraph.Notes.name
    ) {
        composable(SouvenotesScreen.NotesList.name) {
            NotesListRoute(
                onNoteClicked = { noteKey, createdAt ->
                    navHostController.navigate("${SouvenotesScreen.EditNote.name}?noteKey=$noteKey&createdAt=$createdAt")
                },
                onAddNoteClicked = { navHostController.navigate(SouvenotesScreen.EditNote.name) },
                toLoginScreen = {
                    navHostController.navigate(SouvenotesGraph.Auth.name) {
                        popUpTo(SouvenotesScreen.NotesList.name) {
                            inclusive = true
                        }
                    }
                },
                onSettingsClicked = { navHostController.navigate(SouvenotesGraph.UserSettings.name) })
        }
        composable(
            route = "${SouvenotesScreen.EditNote.name}?noteKey={noteKey}&createdAt={createdAt}",
            arguments = listOf(
                navArgument("noteKey") {
                    type = NavType.StringType
                    nullable = true
                }, navArgument("createdAt") {
                    type = NavType.LongType
                    defaultValue = -1L
                })
        ) {
            EditNoteRoute(onNavigateUp = { navHostController.navigateUp() })
        }
    }
}