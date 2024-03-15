package com.akexorcist.privacychecker.ui.accessibility

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun AccessibilityRoute(
    navController: NavController,
) {
    AccessibilityScreen()
}

@Composable
private fun AccessibilityScreen() {
    Text(text = "Accessibility Screen")
}
