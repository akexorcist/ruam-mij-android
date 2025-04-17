package com.akexorcist.ruammij.ui.overview

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.akexorcist.ruammij.ui.Destinations

fun NavController.navigateToOverview() = navigate(Destinations.Overview) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
}

fun NavGraphBuilder.overviewScreen(
    navController: NavController,
) {
    composable<Destinations.Overview> {
        OverviewRoute(
            navController = navController,
        )
    }
}
