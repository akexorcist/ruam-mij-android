package com.akexorcist.ruammij.ui.overview

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val OVERVIEW_ROUTE = "overview_route"

fun NavController.navigateToOverview() = navigate(OVERVIEW_ROUTE) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
}

fun NavGraphBuilder.overviewScreen(
    navController: NavController,
) {
    composable(route = OVERVIEW_ROUTE) {
        OverviewRoute(
            navController = navController,
        )
    }
}
