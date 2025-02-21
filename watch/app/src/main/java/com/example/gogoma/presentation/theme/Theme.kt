package com.example.gogoma.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

@Composable
fun GogomaWatchTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = WearTypography,
        content = content
    )
}

// ✅ Wear OS용 Color 팔레트
private val DarkColorPalette = Colors(
    primary = BrandColor1,
    primaryVariant = BrandColor1_30pct,
    secondary = BrandColor2,
    secondaryVariant = BrandColor3,
    background = NeutralBlack,
    surface = NeutralDark,
    error = CustomRed,
    onPrimary = NeutralWhite,
    onSecondary = NeutralWhite,
    onBackground = NeutralWhite,
    onSurface = NeutralWhite,
    onError = NeutralBlack
)