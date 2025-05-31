package com.akexorcist.ruammij.base.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akexorcist.ruammij.base.ui.theme.RuamMijTheme

@Composable
fun EmptyContent(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = message)
    }
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
private fun EmptyContentPreview() {
    RuamMijTheme {
        EmptyContent(message = "ไม่พบข้อมูล")
    }
}
