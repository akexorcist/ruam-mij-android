package com.akexorcist.privacychecker.ui.aboutapp

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ABOUT_APP_ROUTE = "about_app_route"

fun NavController.navigateToAboutApp(
    navOptions: NavOptions = NavOptions.Builder().build(),
) = navigate(ABOUT_APP_ROUTE, navOptions)

fun NavGraphBuilder.aboutAppScreen(
    navController: NavController,
) {
    composable(route = ABOUT_APP_ROUTE) {
        AboutAppRoute(navController = navController)
    }
}
