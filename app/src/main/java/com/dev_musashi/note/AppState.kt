package com.dev_musashi.note

import android.content.res.Resources
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dev_musashi.note.util.SnackBarManager
import com.dev_musashi.note.util.SnackBarMessage.Companion.toMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.dev_musashi.note.R.string as AppText

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    resources: Resources = resources(),
    snackBarManager: SnackBarManager = SnackBarManager,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(scaffoldState, navController, snackBarManager, resources, coroutineScope) {
    AppState(scaffoldState, navController, snackBarManager, resources, coroutineScope)
}

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@Stable
class AppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val snackBarManager: SnackBarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {

    init {
        coroutineScope.launch {
            val text = snackBarManager.snackMessage.value.toString()
            snackBarManager.snackMessage.collect { messages ->
                if (messages != null && text != messages.toString()) {
                    scaffoldState.snackbarHostState.showSnackbar(messages.toMessage(resources))
                }
            }
        }
    }

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

}
