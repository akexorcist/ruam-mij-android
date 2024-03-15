package com.akexorcist.privacychecker.ui.aboutapp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun AboutAppRoute(
    navController: NavController,
) {
    AboutAppScreen()
}

@Composable
private fun AboutAppScreen() {
    Text(text = "About App Screen")
}
