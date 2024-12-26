package com.akexorcist.ruammij.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val RUAM_MIJ_APP_ROUTE = "ruam_mij_app_route"

fun NavGraphBuilder.ruamMijApp(
    appState: AppState,
) {
    composable(route = RUAM_MIJ_APP_ROUTE) {
        RuamMijApp(
            appState = appState,
        )
    }
}
