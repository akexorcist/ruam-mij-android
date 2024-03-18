package com.akexorcist.ruammij.ui.installedapp

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val INSTALLED_APP_ROUTE = "installed_app_route"
private const val INSTALLED_APP_FULL_ROUTE = "installed_app_route?installer={installer}"

fun NavController.navigateToInstalledApp(installer: String? = null) = navigate(
    when (installer == null) {
        true -> INSTALLED_APP_ROUTE
        false -> "${INSTALLED_APP_ROUTE}?installer=${installer}"
    }
) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
}

fun NavGraphBuilder.installedAppScreen() {
    composable(
        route = INSTALLED_APP_FULL_ROUTE,
        arguments = listOf(navArgument("installer") {
            defaultValue = null
            nullable = true
        })
    ) { backStackEntry ->
        InstalledAppRoute(
            preferredInstaller = backStackEntry.arguments?.getString("installer")
        )
    }
}
