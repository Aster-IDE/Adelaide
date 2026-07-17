package dev.adelaide.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AdelaideDarkColorScheme = darkColorScheme(
    primary = AdelaidePrimary,
    secondary = AdelaideSecondary,
    surface = AdelaideSurface,
    onSurface = AdelaideOnSurface,
    surfaceVariant = AdelaideSurfaceVariant,
    onSurfaceVariant = AdelaideOnSurfaceVariant,
    outline = AdelaideBorder,
)

@Composable
fun AdelaideTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AdelaideDarkColorScheme,
        typography = AdelaideTypography,
        content = content,
    )
}
