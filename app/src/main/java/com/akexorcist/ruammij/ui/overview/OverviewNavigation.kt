package com.akexorcist.ruammij.ui.overview

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.akexorcist.ruammij.functional.core.navigation.Destinations

fun NavGraphBuilder.overviewScreen(
    navController: NavController,
) {
    composable<Destinations.Overview> {
        OverviewRoute(
            navController = navController,
        )
    }
}
