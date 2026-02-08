package com.example.vault.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// TODO: Import 'Outfit' and 'Inter' fonts resources

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif, // Replace with Outfit
        fontWeight = FontWeight.Medium,
        fontSize = 48.sp,
        color = StarlightText
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif, // Replace with Outfit
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        color = StarlightText
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif, // Replace with Inter
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = StarlightText
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif, // Replace with Inter
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        color = MistText,
        letterSpacing = 0.5.sp
    )
)
