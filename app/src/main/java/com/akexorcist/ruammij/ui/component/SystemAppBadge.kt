package com.akexorcist.ruammij.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akexorcist.ruammij.ui.theme.RuamMijTheme
import com.akexorcist.ruammij.utility.DarkLightPreviews

@Composable
fun SystemAppBadge() {
    Badge(
        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        Box(modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)) {
            LabelText(text = "System App")
        }
    }
}

@DarkLightPreviews
@Composable
private fun SystemAppBadgePreview() {
    RuamMijTheme {
        SystemAppBadge()
    }
}