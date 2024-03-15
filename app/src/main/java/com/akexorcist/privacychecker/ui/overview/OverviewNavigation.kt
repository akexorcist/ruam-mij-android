package com.akexorcist.privacychecker.ui.overview

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val OVERVIEW_ROUTE = "overview_route"

fun NavController.navigateToOverview(
    navOptions: NavOptions = NavOptions.Builder().build(),
) = navigate(OVERVIEW_ROUTE, navOptions)

fun NavGraphBuilder.overviewScreen(
    navController: NavController,
) {
    composable(route = OVERVIEW_ROUTE) {
        OverviewRoute(navController = navController)
    }
}
