package com.akexorcist.ruammij.ui.aboutapp

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.akexorcist.ruammij.ui.AppState

const val ABOUT_APP_ROUTE = "about_app_route"

fun NavController.navigateToAboutApp() = navigate(ABOUT_APP_ROUTE) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
}

fun NavGraphBuilder.aboutAppScreen(
    appState: AppState,
) {
    composable(route = ABOUT_APP_ROUTE) {
        AboutAppRoute(
            appState = appState,
        )
    }
}
