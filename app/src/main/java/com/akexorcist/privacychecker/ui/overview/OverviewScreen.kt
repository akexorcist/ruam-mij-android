package com.akexorcist.privacychecker.ui.overview

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun OverviewRoute(
    navController: NavController,
) {
    OverviewScreen()
}

@Composable
private fun OverviewScreen() {
    Text(text = "Overview Screen")
}
