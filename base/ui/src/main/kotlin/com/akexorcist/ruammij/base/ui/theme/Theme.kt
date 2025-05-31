package com.akexorcist.ruammij.base.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Colors.DarkPrimary200,
    onPrimary = Colors.Text800,
    primaryContainer = Colors.DarkPrimary900,
    onPrimaryContainer = Colors.Text50,
    inversePrimary = Colors.DarkPrimary100,

    secondary = Colors.DarkPrimary100,
    onSecondary = Colors.Text800,
    secondaryContainer = Colors.DarkPrimary800,
    onSecondaryContainer = Colors.Text50,

    tertiary = Colors.Gray200,
    onTertiary = Colors.Text700,
    tertiaryContainer = Colors.Gray800,
    onTertiaryContainer = Colors.Text50,

    background = Colors.Gray900,
    onBackground = Colors.Text100,
    surface = Colors.DarkPrimary800,
    onSurface = Colors.Text100,
//    surfaceVariant =,
//    onSurfaceVariant =,
//    surfaceTint =,
//    inverseSurface =,
//    inverseOnSurface =,
    error = Colors.Negative300,
    onError = Colors.Text800,
    errorContainer = Colors.Negative800,
    onErrorContainer = Colors.Text50,
    outline = Colors.Gray500,
//    outlineVariant =,
//    scrim =,
//    surfaceBright =,
    surfaceContainer = Colors.Gray850,
//    surfaceContainerHigh =,
//    surfaceContainerHighest =,
//    surfaceContainerLow =,
//    surfaceContainerLowest =,
//    surfaceDim =,
)

private val LightColorScheme = lightColorScheme(
    primary = Colors.LightPrimary800,
    onPrimary = Colors.Text50,
    primaryContainer = Colors.LightPrimary100,
    onPrimaryContainer = Colors.Text700,
    inversePrimary = Colors.LightPrimary900,

    secondary = Colors.LightPrimary100,
    onSecondary = Colors.Text800,
    secondaryContainer = Colors.LightPrimary50,
    onSecondaryContainer = Colors.Text700,

    tertiary = Colors.Gray600,
    onTertiary = Colors.Text50,
    tertiaryContainer = Colors.Gray100,
    onTertiaryContainer = Colors.Text700,

    background = Colors.White,
    onBackground = Colors.Text700,
    surface = Colors.LightPrimary50,
    onSurface = Colors.Text700,
//    surfaceVariant =,
//    onSurfaceVariant =,
//    surfaceTint =,
//    inverseSurface =,
//    inverseOnSurface =,
    error = Colors.Negative600,
    onError = Colors.Text50,
    errorContainer = Colors.Negative100,
    onErrorContainer = Colors.Text800,
    outline = Colors.Gray500,
//    outlineVariant =,
//    scrim =,
//    surfaceBright =,
    surfaceContainer = Colors.LightPrimary10,
//    surfaceContainerHigh =,
//    surfaceContainerHighest =,
//    surfaceContainerLow =,
//    surfaceContainerLowest =,
//    surfaceDim =,
)

@Composable
fun RuamMijTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val additionColorScheme = when {
        darkTheme -> AdditionColorScheme(
            warning = Colors.Warning600,
            onWarning = Colors.Text50,
            warningContainer = Colors.Warning800,
            onWarningContainer = Colors.Text50,

            success = Colors.Positive600,
            onSuccess = Colors.Text50,
            successContainer = Colors.Positive800,
            onSuccessContainer = Colors.Text50,
        )

        else -> AdditionColorScheme(
            warning = Colors.Warning500,
            onWarning = Colors.Text800,
            warningContainer = Colors.Warning200,
            onWarningContainer = Colors.Text700,

            success = Colors.Positive300,
            onSuccess = Colors.Text800,
            successContainer = Colors.Positive100,
            onSuccessContainer = Colors.Text700,
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            SideEffect {
                val window = (view.context as Activity).window
                @Suppress("DEPRECATION")
                window.statusBarColor = colorScheme.primary.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
            }
        }
    }
    CompositionLocalProvider(
        values = arrayOf(
            LocalAdditionColorScheme provides additionColorScheme
        )
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

val LocalAdditionColorScheme = compositionLocalOf {
    AdditionColorScheme(
        warning = Color.Unspecified,
        onWarning = Color.Unspecified,
        warningContainer = Color.Unspecified,
        onWarningContainer = Color.Unspecified,
        success = Color.Unspecified,
        onSuccess = Color.Unspecified,
        successContainer = Color.Unspecified,
        onSuccessContainer = Color.Unspecified,
    )
}

data class AdditionColorScheme(
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,

    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
)

object MaterialAdditionColorScheme {
    val colorScheme: AdditionColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAdditionColorScheme.current
}
