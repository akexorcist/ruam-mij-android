package com.akexorcist.ruammij.ui.installedapp

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.akexorcist.ruammij.functional.core.navigation.Destinations

fun NavGraphBuilder.installedAppScreen() {
    composable<Destinations.InstalledApp> { backStackEntry ->
        InstalledAppRoute(
            preferredInstaller = backStackEntry.arguments?.getString("installer"),
            preferredShowSystemApp = backStackEntry.arguments?.getBoolean("showSystemApp"),
        )
    }
}
