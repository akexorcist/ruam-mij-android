package com.akexorcist.privacychecker.ui.installedapp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun InstalledAppRoute(
    navController: NavController,
) {
    InstalledAppScreen()
}

@Composable
private fun InstalledAppScreen() {
    Text(text = "Installed App Screen")
}
