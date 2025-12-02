package com.akexorcist.ruammij.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.akexorcist.ruammij.feature.aboutapp.AboutAppRoute
import com.akexorcist.ruammij.feature.accessibility.AccessibilityRoute
import com.akexorcist.ruammij.feature.installedapp.InstalledAppRoute
import com.akexorcist.ruammij.feature.osslicense.OpenSourceLicenseRoute
import com.akexorcist.ruammij.feature.overview.OverviewRoute
import com.akexorcist.ruammij.functional.core.navigation.BottomBarNavController
import com.akexorcist.ruammij.functional.core.navigation.Destinations
import com.akexorcist.ruammij.functional.core.state.AppState

fun NavGraphBuilder.ruamMijApp(
    appState: AppState,
) {
    composable<Destinations.Root> {
        RuamMijApp(
            appState = appState,
        )
    }
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

fun NavGraphBuilder.accessibilityScreen() {
    composable<Destinations.Accessibility> {
        AccessibilityRoute()
    }
}

fun NavGraphBuilder.installedAppScreen() {
    composable<Destinations.InstalledApp> { backStackEntry ->
        val route = backStackEntry.toRoute<Destinations.InstalledApp>()
        val preferredPackageName = route.packageName
        val preferredInstaller = route.installer
        val preferredShowSystemApp = route.showSystemApp
        InstalledAppRoute(
            preferredPackageName = preferredPackageName,
            preferredInstaller = preferredInstaller,
            preferredShowSystemApp = preferredShowSystemApp,
        )
    }
}

fun NavGraphBuilder.openSourceLicenseScreen(
    navController: NavController,
) {
    composable<Destinations.OpenSourceLicense>(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(350),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(350),
            )
        },
    ) {
        OpenSourceLicenseRoute(
            navController = navController,
        )
    }
}

fun NavGraphBuilder.overviewScreen(
    navController: BottomBarNavController,
) {
    composable<Destinations.Overview> {
        OverviewRoute(
            navController = navController,
        )
    }
}
