package com.akexorcist.ruammij.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.akexorcist.ruammij.R
import com.akexorcist.ruammij.ui.aboutapp.aboutAppScreen
import com.akexorcist.ruammij.ui.aboutapp.navigateToAboutApp
import com.akexorcist.ruammij.ui.accessibility.accessibilityScreen
import com.akexorcist.ruammij.ui.accessibility.navigateToAccessibility
import com.akexorcist.ruammij.ui.component.DescriptionText
import com.akexorcist.ruammij.ui.component.HeadlineText
import com.akexorcist.ruammij.ui.installedapp.installedAppScreen
import com.akexorcist.ruammij.ui.installedapp.navigateToInstalledApp
import com.akexorcist.ruammij.ui.overview.navigateToOverview
import com.akexorcist.ruammij.ui.overview.overviewScreen
import com.akexorcist.ruammij.ui.theme.Buttons
import com.akexorcist.ruammij.ui.theme.RuamMijTheme

@Composable
fun RuamMijApp(
    appState: AppState,
) {
    val navController = appState.bottomBarNavController

    Scaffold(
        bottomBar = {
            BottomMenu(
                currentDestination = appState.currentDestination.toBottomMenuDestination(),
                onDestinationSelected = {
                    when (it) {
                        BottomMenuDestination.Overview -> navController.navigateToOverview()
                        BottomMenuDestination.Accessibility -> navController.navigateToAccessibility()
                        BottomMenuDestination.InstalledApp -> navController.navigateToInstalledApp()
                        BottomMenuDestination.AboutApp -> navController.navigateToAboutApp()
                    }
                },
            )
        },
        content = { padding ->
            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                navController = appState.bottomBarNavController,
                startDestination = Destinations.Overview,
            ) {
                overviewScreen(navController = navController)
                accessibilityScreen()
                installedAppScreen()
                aboutAppScreen(appState = appState)
            }
        },
    )
}

@Composable
private fun UnverifiedInstallerContent(
    onDownloadButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center,
    ) {
        HeadlineText(text = stringResource(R.string.landing_tips_title))
        Spacer(modifier = Modifier.height(8.dp))
        DescriptionText(text = stringResource(R.string.landing_tips_description))
        Spacer(modifier = Modifier.height(16.dp))
        DownloadButton(onClick = onDownloadButtonClick)
    }
}

@Composable
private fun DownloadButton(onClick: () -> Unit) {
    OutlinedButton(
        contentPadding = Buttons.ContentPadding,
        onClick = onClick,
    ) {
        Text(text = stringResource(R.string.landing_tips_open_google_play))
    }
}

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

@Preview
@Composable
private fun RuamMijAppPreview() {
    RuamMijTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            UnverifiedInstallerContent(
                onDownloadButtonClick = {},
            )
        }
    }
}
