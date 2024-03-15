package com.akexorcist.privacychecker.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import com.akexorcist.privacychecker.R
import com.akexorcist.privacychecker.ui.aboutapp.ABOUT_APP_ROUTE
import com.akexorcist.privacychecker.ui.accessibility.ACCESSIBILITY_ROUTE
import com.akexorcist.privacychecker.ui.installedapp.INSTALLED_APP_ROUTE
import com.akexorcist.privacychecker.ui.overview.OVERVIEW_ROUTE

@Composable
fun BottomMenu(
    destinations: List<BottomMenuDestination>,
    currentDestination: BottomMenuDestination,
    onDestinationSelected: (BottomMenuDestination) -> Unit,
) {
    NavigationBar {
        destinations.forEach {
            BottomMenuItem(
                destination = it,
                onDestinationSelected = onDestinationSelected,
            )
        }
    }
}

@Composable
fun BottomMenuItem(
    destination: BottomMenuDestination,
    onDestinationSelected: (BottomMenuDestination) -> Unit,
) {
    Column(
        modifier = Modifier.clickable { onDestinationSelected(destination) }
    ) {
        Text(text = stringResource(destination.label))
    }
}

sealed class BottomMenuDestination(
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    @StringRes val label: Int,
) {
    data object Overview : BottomMenuDestination(
        selectedIcon = 0,
        unselectedIcon = 0,
        label = R.string.menu_overview,
    )

    data object Accessibility : BottomMenuDestination(
        selectedIcon = 0,
        unselectedIcon = 0,
        label = R.string.menu_accessibility,
    )

    data object InstalledApp : BottomMenuDestination(
        selectedIcon = 0,
        unselectedIcon = 0,
        label = R.string.menu_installed_app,
    )

    data object AboutApp : BottomMenuDestination(
        selectedIcon = 0,
        unselectedIcon = 0,
        label = R.string.menu_about_app,
    )
}


fun NavDestination?.toBottomMenuDestination(): BottomMenuDestination = when (this?.route) {
    OVERVIEW_ROUTE -> BottomMenuDestination.Overview
    ACCESSIBILITY_ROUTE -> BottomMenuDestination.Accessibility
    INSTALLED_APP_ROUTE -> BottomMenuDestination.InstalledApp
    ABOUT_APP_ROUTE -> BottomMenuDestination.AboutApp
    else -> BottomMenuDestination.Overview
}