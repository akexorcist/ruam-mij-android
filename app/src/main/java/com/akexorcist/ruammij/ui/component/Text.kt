package com.akexorcist.ruammij.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun BodyText(
    text: String,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        color = color,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight,
        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
        letterSpacing = MaterialTheme.typography.bodyMedium.letterSpacing,
        textAlign = textAlign,
    )
}

@Composable
fun BoldBodyText(
    text: String,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        color = color,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight,
        fontWeight = FontWeight.Bold,
        letterSpacing = MaterialTheme.typography.bodyMedium.letterSpacing,
        textAlign = textAlign,
    )
}

@Composable
fun DescriptionText(
    text: String,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
) {
    BodyText(
        text = text,
        color = color,
        textAlign = textAlign,
    )
}

@Composable
fun LabelText(
    text: String,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        color = color,
        fontSize = MaterialTheme.typography.labelSmall.fontSize,
        fontStyle = MaterialTheme.typography.labelSmall.fontStyle,
        lineHeight = MaterialTheme.typography.labelSmall.lineHeight,
        fontWeight = MaterialTheme.typography.labelSmall.fontWeight,
        letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing,
        textAlign = textAlign,
    )
}

@Composable
fun BoldLabelText(
    text: String,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        fontSize = MaterialTheme.typography.labelSmall.fontSize,
        fontStyle = MaterialTheme.typography.labelSmall.fontStyle,
        lineHeight = MaterialTheme.typography.labelSmall.lineHeight,
        fontWeight = FontWeight.Bold,
        letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing,
        textAlign = textAlign,
    )
}

@Composable
fun HeadlineText(
    text: String,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
        fontStyle = MaterialTheme.typography.headlineSmall.fontStyle,
        lineHeight = MaterialTheme.typography.headlineSmall.lineHeight,
        fontWeight = MaterialTheme.typography.headlineSmall.fontWeight,
        letterSpacing = MaterialTheme.typography.headlineSmall.letterSpacing,
        textAlign = textAlign,
    )
}

@Composable
fun TitleText(
    text: String,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
        lineHeight = MaterialTheme.typography.titleMedium.lineHeight,
        fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
        letterSpacing = MaterialTheme.typography.titleMedium.letterSpacing,
        textAlign = textAlign,
    )
}
