package com.akexorcist.ruammij.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.akexorcist.ruammij.R
import com.akexorcist.ruammij.base.ui.component.BoldLabelText
import com.akexorcist.ruammij.base.ui.theme.RuamMijTheme

@Composable
fun BottomMenu(
    currentDestination: BottomMenuDestination,
    onDestinationSelected: (BottomMenuDestination) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Spacer(modifier = Modifier.width(2.dp))
        listOf(
            BottomMenuDestination.Overview,
            BottomMenuDestination.Accessibility,
            BottomMenuDestination.InstalledApp,
            BottomMenuDestination.AboutApp,
        ).forEach {
            BottomMenuItem(
                selected = currentDestination == it,
                destination = it,
                onDestinationSelected = onDestinationSelected,
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
    }
}

@Composable
fun RowScope.BottomMenuItem(
    selected: Boolean,
    destination: BottomMenuDestination,
    onDestinationSelected: (BottomMenuDestination) -> Unit,
) {
    NavigationBarItem(
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.tertiary,
            unselectedTextColor = MaterialTheme.colorScheme.tertiary,
            disabledIconColor = MaterialTheme.colorScheme.surface,
            disabledTextColor = MaterialTheme.colorScheme.surface,
        ),
        label = {
            BoldLabelText(
                text = stringResource(destination.label),
                textAlign = TextAlign.Center,
            )
        },
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(destination.icon),
                contentDescription = stringResource(destination.label),
            )
        },
        onClick = { onDestinationSelected(destination) },
        selected = selected,
    )
}

@Preview
@Composable
private fun BottomMenuPreview() {
    RuamMijTheme {
        BottomMenu(
            currentDestination = BottomMenuDestination.Overview,
            onDestinationSelected = { }
        )
    }
}

sealed class BottomMenuDestination(
    @DrawableRes val icon: Int,
    @StringRes val label: Int,
) {
    data object Overview : BottomMenuDestination(
        icon = R.drawable.ic_menu_overview,
        label = R.string.menu_overview,
    )

    data object Accessibility : BottomMenuDestination(
        icon = R.drawable.ic_menu_accessibility,
        label = R.string.menu_accessibility,
    )

    data object InstalledApp : BottomMenuDestination(
        icon = R.drawable.ic_menu_installed_app,
        label = R.string.menu_installed_app,
    )

    data object AboutApp : BottomMenuDestination(
        icon = R.drawable.ic_menu_about_app,
        label = R.string.menu_about_app,
    )
}


fun NavDestination?.toBottomMenuDestination(): BottomMenuDestination = when {
    this == null -> BottomMenuDestination.Overview
    this.hasRoute<Destinations.Overview>() -> BottomMenuDestination.Overview
    this.hasRoute<Destinations.Accessibility>() -> BottomMenuDestination.Accessibility
    this.hasRoute<Destinations.InstalledApp>() -> BottomMenuDestination.InstalledApp
    this.hasRoute<Destinations.AboutApp>() -> BottomMenuDestination.AboutApp
    else -> BottomMenuDestination.Overview
}
