package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = LiliaPrimary,
    onPrimary = LiliaOnPrimary,
    primaryContainer = LiliaPrimaryContainer,
    onPrimaryContainer = LiliaOnPrimaryContainer,
    inversePrimary = LiliaInversePrimary,
    secondary = LiliaSecondary,
    onSecondary = LiliaOnSecondary,
    secondaryContainer = LiliaSecondaryContainer,
    onSecondaryContainer = LiliaOnSecondaryContainer,
    tertiary = LiliaTertiary,
    onTertiary = LiliaOnTertiary,
    tertiaryContainer = LiliaTertiaryContainer,
    onTertiaryContainer = LiliaOnTertiaryContainer,
    background = LiliaBackground,
    onBackground = LiliaOnBackground,
    surface = LiliaSurface,
    onSurface = LiliaOnSurface,
    surfaceVariant = LiliaSurfaceVariant,
    onSurfaceVariant = LiliaOnSurfaceVariant,
    surfaceTint = LiliaSurfaceTint,
    inverseSurface = LiliaInverseSurface,
    inverseOnSurface = LiliaInverseOnSurface,
    outline = LiliaOutline,
    outlineVariant = LiliaOutlineVariant,
    error = LiliaError,
    onError = LiliaOnError,
    errorContainer = LiliaErrorContainer,
    onErrorContainer = LiliaOnErrorContainer,
    surfaceBright = LiliaSurfaceBright,
    surfaceDim = LiliaSurfaceDim,
    surfaceContainerLowest = LiliaSurfaceContainerLowest,
    surfaceContainerLow = LiliaSurfaceContainerLow,
    surfaceContainer = LiliaSurfaceContainer,
    surfaceContainerHigh = LiliaSurfaceContainerHigh,
    surfaceContainerHighest = LiliaSurfaceContainerHighest
)

private val DarkColorScheme = lightColorScheme(
    primary = LiliaPrimaryFixedDim,
    onPrimary = LiliaOnPrimary,
    primaryContainer = LiliaPrimaryContainer,
    onPrimaryContainer = LiliaOnPrimaryContainer,
    inversePrimary = LiliaInversePrimary,
    secondary = LiliaSecondaryFixedDim,
    onSecondary = LiliaOnSecondary,
    secondaryContainer = LiliaSecondaryContainer,
    onSecondaryContainer = LiliaOnSecondaryContainer,
    tertiary = LiliaTertiaryFixedDim,
    onTertiary = LiliaOnTertiary,
    tertiaryContainer = LiliaTertiaryContainer,
    onTertiaryContainer = LiliaOnTertiaryContainer,
    background = LiliaBackground,
    onBackground = LiliaOnBackground,
    surface = LiliaSurface,
    onSurface = LiliaOnSurface,
    surfaceVariant = LiliaSurfaceVariant,
    onSurfaceVariant = LiliaOnSurfaceVariant,
    surfaceTint = LiliaSurfaceTint,
    inverseSurface = LiliaInverseSurface,
    inverseOnSurface = LiliaInverseOnSurface,
    outline = LiliaOutline,
    outlineVariant = LiliaOutlineVariant,
    error = LiliaError,
    onError = LiliaOnError,
    errorContainer = LiliaErrorContainer,
    onErrorContainer = LiliaOnErrorContainer,
    surfaceBright = LiliaSurfaceBright,
    surfaceDim = LiliaSurfaceDim,
    surfaceContainerLowest = LiliaSurfaceContainerLowest,
    surfaceContainerLow = LiliaSurfaceContainerLow,
    surfaceContainer = LiliaSurfaceContainer,
    surfaceContainerHigh = LiliaSurfaceContainerHigh,
    surfaceContainerHighest = LiliaSurfaceContainerHighest
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = LiliaShapes,
        content = content
    )
}

