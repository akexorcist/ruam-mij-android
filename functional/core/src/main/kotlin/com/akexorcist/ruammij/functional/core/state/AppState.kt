package com.akexorcist.ruammij.functional.core.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Stable
class AppState(
    val mainNavController: NavHostController,
    val bottomBarNavController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = bottomBarNavController.currentBackStackEntryAsState().value?.destination
}

@Composable
fun rememberAppState(
    mainNavController: NavHostController = rememberNavController(),
    bottomBarNavController: NavHostController = rememberNavController(),
): AppState {
    return remember(mainNavController, bottomBarNavController) {
        AppState(mainNavController, bottomBarNavController)
    }
}
