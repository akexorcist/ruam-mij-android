package com.akexorcist.ruammij.ui.osslicense

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.akexorcist.ruammij.ui.Destinations

fun NavController.navigateToOpenSourceLicense() = navigate(Destinations.OpenSourceLicense) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
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
