package com.akexorcist.ruammij.ui.aboutapp

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.akexorcist.ruammij.ui.AppState
import com.akexorcist.ruammij.ui.Destinations

fun NavController.navigateToAboutApp() = navigate(Destinations.AboutApp) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
}

fun NavGraphBuilder.aboutAppScreen(
    appState: AppState,
) {
    composable<Destinations.AboutApp> {
        AboutAppRoute(
            appState = appState,
        )
    }
}
