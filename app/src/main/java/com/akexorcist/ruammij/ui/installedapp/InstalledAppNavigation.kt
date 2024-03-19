package com.akexorcist.ruammij.ui.installedapp

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val INSTALLED_APP_ROUTE = "installed_app_route"
private const val INSTALLED_APP_FULL_ROUTE = "installed_app_route?installer={installer}&showSystemApp={showSystemApp}"

fun NavController.navigateToInstalledApp(
    installer: String? = null,
    showSystemApp: Boolean = false,
) = navigate(
    when (installer == null) {
        true -> INSTALLED_APP_ROUTE
        false -> "${INSTALLED_APP_ROUTE}?installer=${installer}&showSystemApp=${showSystemApp}"
    }
) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
}

fun NavGraphBuilder.installedAppScreen() {
    composable(
        route = INSTALLED_APP_FULL_ROUTE,
        arguments = listOf(
            navArgument("installer") {
                this.type = NavType.StringType
                defaultValue = null
                nullable = true
            },
            navArgument("showSystemApp") {
                this.type = NavType.BoolType
                defaultValue = false
            },
        )
    ) { backStackEntry ->
        InstalledAppRoute(
            preferredInstaller = backStackEntry.arguments?.getString("installer"),
            preferredShowSystemApp = backStackEntry.arguments?.getBoolean("showSystemApp"),
        )
    }
}
