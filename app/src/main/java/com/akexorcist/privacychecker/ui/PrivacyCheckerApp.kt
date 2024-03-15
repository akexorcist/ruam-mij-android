package com.akexorcist.privacychecker.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.akexorcist.privacychecker.ui.aboutapp.aboutAppScreen
import com.akexorcist.privacychecker.ui.aboutapp.navigateToAboutApp
import com.akexorcist.privacychecker.ui.accessibility.accessibilityScreen
import com.akexorcist.privacychecker.ui.accessibility.navigateToAccessibility
import com.akexorcist.privacychecker.ui.installedapp.installedAppScreen
import com.akexorcist.privacychecker.ui.installedapp.navigateToInstalledApp
import com.akexorcist.privacychecker.ui.overview.OVERVIEW_ROUTE
import com.akexorcist.privacychecker.ui.overview.navigateToOverview
import com.akexorcist.privacychecker.ui.overview.overviewScreen

@Composable
fun PrivacyCheckerApp(
    appState: AppState,
) {
    val navController = appState.navController
    Scaffold(
        bottomBar = {
            BottomMenu(
                destinations = listOf(
                    BottomMenuDestination.Overview,
                    BottomMenuDestination.Accessibility,
                    BottomMenuDestination.InstalledApp,
                    BottomMenuDestination.AboutApp,
                ),
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
                modifier = Modifier.padding(padding),
                navController = appState.navController,
                startDestination = OVERVIEW_ROUTE,
            ) {
                overviewScreen(navController)
                accessibilityScreen(navController)
                installedAppScreen(navController)
                aboutAppScreen(navController)
            }
        },
    )
}

@Stable
class AppState(
    val navController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
): AppState {
    return remember(navController) {
        AppState(navController)
    }
}
