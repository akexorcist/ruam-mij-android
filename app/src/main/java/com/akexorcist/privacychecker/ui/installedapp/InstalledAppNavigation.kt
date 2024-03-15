package com.akexorcist.privacychecker.ui.installedapp

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val INSTALLED_APP_ROUTE = "installed_app_route"

fun NavController.navigateToInstalledApp(
    navOptions: NavOptions = NavOptions.Builder().build(),
) = navigate(INSTALLED_APP_ROUTE, navOptions)

fun NavGraphBuilder.installedAppScreen(
    navController: NavController,
) {
    composable(route = INSTALLED_APP_ROUTE) {
        InstalledAppRoute(navController = navController)
    }
}
