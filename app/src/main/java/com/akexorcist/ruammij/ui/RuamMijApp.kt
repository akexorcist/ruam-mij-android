package com.akexorcist.ruammij.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.akexorcist.ruammij.functional.core.navigation.Destinations
import com.akexorcist.ruammij.functional.core.navigation.navigateToAboutApp
import com.akexorcist.ruammij.functional.core.navigation.navigateToAccessibility
import com.akexorcist.ruammij.functional.core.navigation.navigateToInstalledApp
import com.akexorcist.ruammij.functional.core.navigation.navigateToOverview
import com.akexorcist.ruammij.functional.core.state.AppState

@Composable
fun RuamMijApp(
    appState: AppState,
) {
    val navController = appState.bottomBarNavController

    Scaffold(
        bottomBar = {
            BottomMenu(
                currentDestination = appState.currentDestination.toBottomMenuDestination(),
                onDestinationSelected = {
                    when (it) {
                        BottomMenuDestination.Overview -> navController.navigateToOverview()
                        BottomMenuDestination.Accessibility -> navController.navigateToAccessibility()
                        BottomMenuDestination.InstalledApp -> navController.navigateToInstalledApp()
                        BottomMenuDestination.AboutApp -> navController.navigateToAboutApp()
                    }
                },
            )
        },
        content = { padding ->
            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                navController = appState.bottomBarNavController,
                startDestination = Destinations.Overview,
            ) {
                overviewScreen(navController = navController)
                accessibilityScreen()
                installedAppScreen()
                aboutAppScreen(appState = appState)
            }
        },
    )
}
