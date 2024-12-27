package com.akexorcist.ruammij.ui.osslicense

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val OPEN_SOURCE_LICENSES_ROUTE = "open_source_licenses_route"

fun NavController.navigateToOpenSourceLicense() = navigate(route = OPEN_SOURCE_LICENSES_ROUTE) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
}

fun NavGraphBuilder.openSourceLicenseScreen(
    navController: NavController,
) {
    composable(
        route = OPEN_SOURCE_LICENSES_ROUTE,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(350))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(350))
        },
    ) {
        OpenSourceLicenseRoute(
            navController = navController,
        )
    }
}
