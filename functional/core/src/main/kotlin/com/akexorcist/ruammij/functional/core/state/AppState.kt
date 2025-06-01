package com.akexorcist.ruammij.functional.core.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.akexorcist.ruammij.functional.core.navigation.BottomBarNavController
import com.akexorcist.ruammij.functional.core.navigation.MainNavController

@Stable
class AppState(
    val mainNavController: MainNavController,
    val bottomBarNavController: BottomBarNavController,
) {
    val currentBottomMenuDestination: NavDestination?
        @Composable get() = bottomBarNavController.controller.currentBackStackEntryAsState().value?.destination
}

@Composable
fun rememberAppState(
    mainNavController: NavHostController = rememberNavController(),
    bottomBarNavController: NavHostController = rememberNavController(),
): AppState {
    return remember(mainNavController, bottomBarNavController) {
        AppState(
            MainNavController(mainNavController),
            BottomBarNavController(bottomBarNavController),
        )
    }
}
