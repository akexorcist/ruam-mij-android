package com.akexorcist.ruammij.ui.aboutapp

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.akexorcist.ruammij.feature.aboutapp.AboutAppRoute
import com.akexorcist.ruammij.functional.core.navigation.Destinations
import com.akexorcist.ruammij.functional.core.state.AppState

fun NavGraphBuilder.aboutAppScreen(
    appState: AppState,
) {
    composable<Destinations.AboutApp> {
        AboutAppRoute(
            appState = appState,
        )
    }
}
