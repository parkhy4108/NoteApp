package com.dev_musashi.note

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dev_musashi.note.presentation.add.NoteScreen
import com.dev_musashi.note.presentation.main.MainScreen
import com.dev_musashi.note.presentation.splash.SplashScreen
import com.dev_musashi.note.ui.theme.NoteTheme

@Composable
fun NoteApp() {
    NoteTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val appState = rememberAppState()
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = it,
                        modifier = Modifier,
                        snackbar = { snackBarData ->
                            Snackbar(snackBarData)
                        }
                    )
                },
                scaffoldState = appState.scaffoldState,
            ) { innerPadding ->
                NavHost(
                    navController = appState.navController,
                    startDestination = Screen.Splash.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    graph(appState)
                }
            }
        }
    }
}

fun NavGraphBuilder.graph(appState: AppState) {
    composable(route = Screen.Splash.route) {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route,popUp)})
    }
    composable(route = Screen.Main.route) {
        MainScreen(
            openScreen = { route -> appState.navigate(route) }
        )
    }
    composable(
        route = Screen.Note.route,
        arguments = listOf(
            navArgument(ARG_ID) {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) {
        val id = it.arguments?.getInt(ARG_ID)!!
        NoteScreen(
            id = id,
            popUpScreen = { appState.popUp() }
        )
    }
}