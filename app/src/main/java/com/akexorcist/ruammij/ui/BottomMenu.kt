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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.akexorcist.ruammij.R
import com.akexorcist.ruammij.ui.aboutapp.ABOUT_APP_ROUTE
import com.akexorcist.ruammij.ui.accessibility.ACCESSIBILITY_ROUTE
import com.akexorcist.ruammij.ui.component.LabelText
import com.akexorcist.ruammij.ui.installedapp.INSTALLED_APP_ROUTE
import com.akexorcist.ruammij.ui.overview.OVERVIEW_ROUTE
import com.akexorcist.ruammij.ui.theme.RuamMijTheme

@Composable
fun BottomMenu(
    currentDestination: BottomMenuDestination,
    onDestinationSelected: (BottomMenuDestination) -> Unit,
) {
    NavigationBar {
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
        label = {
            LabelText(
                text = stringResource(destination.label),
                color = when (selected) {
                    true -> MaterialTheme.colorScheme.primary
                    false -> MaterialTheme.colorScheme.outline
                },
                textAlign = TextAlign.Center,
            )
        },
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(destination.icon),
                contentDescription = stringResource(destination.label),
                tint = when (selected) {
                    true -> MaterialTheme.colorScheme.primary
                    false -> MaterialTheme.colorScheme.outline
                },
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


fun NavDestination?.toBottomMenuDestination(): BottomMenuDestination = when (this?.route?.split("?")?.first()) {
    OVERVIEW_ROUTE -> BottomMenuDestination.Overview
    ACCESSIBILITY_ROUTE -> BottomMenuDestination.Accessibility
    INSTALLED_APP_ROUTE -> BottomMenuDestination.InstalledApp
    ABOUT_APP_ROUTE -> BottomMenuDestination.AboutApp
    else -> BottomMenuDestination.Overview
}