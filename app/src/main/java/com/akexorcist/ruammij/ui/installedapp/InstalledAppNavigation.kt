package com.akexorcist.ruammij.ui.installedapp

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.akexorcist.ruammij.ui.Destinations

fun NavController.navigateToInstalledApp(
    installer: String? = null,
    showSystemApp: Boolean = false,
) = navigate(
    Destinations.InstalledApp(
        installer = installer,
        showSystemApp = showSystemApp,
    )
) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
}

fun NavGraphBuilder.installedAppScreen() {
    composable<Destinations.InstalledApp> { backStackEntry ->
        InstalledAppRoute(
            preferredInstaller = backStackEntry.arguments?.getString("installer"),
            preferredShowSystemApp = backStackEntry.arguments?.getBoolean("showSystemApp"),
        )
    }
}
