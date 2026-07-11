package com.akexorcist.ruammij.base.ui.component

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.core.graphics.drawable.toBitmap

@Composable
fun rememberAppIconPainter(icon: Drawable?): Painter {
    return remember(icon) {
        icon?.toBitmap()?.asImageBitmap()?.let(::BitmapPainter)
            ?: ColorPainter(Color.Transparent)
    }
}
