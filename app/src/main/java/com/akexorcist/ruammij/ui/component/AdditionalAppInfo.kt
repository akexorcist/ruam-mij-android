package com.akexorcist.ruammij.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akexorcist.ruammij.ui.theme.RuamMijTheme
import com.akexorcist.ruammij.base.utility.DarkLightPreviews

@Composable
fun AdditionalAppInfo(
    label: String,
    value: String,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    valueContent: @Composable () -> Unit = {
        BodyText(text = value, color = MaterialTheme.colorScheme.onBackground)
    }
) {
    Row(verticalAlignment = verticalAlignment) {
        Box(modifier = Modifier.width(80.dp)) {
            BoldBodyText(text = label, color = MaterialTheme.colorScheme.onBackground)
        }
        Spacer(modifier = Modifier.width(4.dp))
        valueContent()
    }
}

@DarkLightPreviews
@Composable
private fun AdditionalAppInfoPreview() {
    RuamMijTheme {
        AdditionalAppInfo(
            label = "A label",
            value = "This is a value",
        )
    }
}