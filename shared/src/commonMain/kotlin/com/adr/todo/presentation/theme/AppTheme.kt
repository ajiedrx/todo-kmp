package com.adr.todo.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object AppColors {
    val LightPrimary = Color(0xFF5B7FBD)
    val LightOnPrimary = Color.White
    val LightPrimaryContainer = Color(0xFFD6E3FF)
    val LightOnPrimaryContainer = Color(0xFF001A40)
    val LightSecondary = Color(0xFF6A5FA7)
    val LightOnSecondary = Color.White
    val LightSecondaryContainer = Color(0xFFE9DDFF)
    val LightOnSecondaryContainer = Color(0xFF21005E)
    val LightBackground = Color(0xFFF8F9FA)
    val LightOnBackground = Color(0xFF1B1B1F)
    val LightSurface = Color.White
    val LightOnSurface = Color(0xFF1B1B1F)
    val LightSurfaceVariant = Color(0xFFE0E2EC)
    val LightOnSurfaceVariant = Color(0xFF44474F)
    val LightOutline = Color(0xFF74777F)

    val DarkPrimary = Color(0xFFACC7FF)
    val DarkOnPrimary = Color(0xFF002F68)
    val DarkPrimaryContainer = Color(0xFF004494)
    val DarkOnPrimaryContainer = Color(0xFFD6E3FF)
    val DarkSecondary = Color(0xFFCEBDFF)
    val DarkOnSecondary = Color(0xFF371E72)
    val DarkSecondaryContainer = Color(0xFF4F378B)
    val DarkOnSecondaryContainer = Color(0xFFE9DDFF)
    val DarkBackground = Color(0xFF1B1B1F)
    val DarkOnBackground = Color(0xFFE3E2E6)
    val DarkSurface = Color(0xFF121316)
    val DarkOnSurface = Color(0xFFE3E2E6)
    val DarkSurfaceVariant = Color(0xFF44474F)
    val DarkOnSurfaceVariant = Color(0xFFC4C6D0)
    val DarkOutline = Color(0xFF8E9099)

    val Error = Color(0xFFBA1A1A)
    val OnError = Color.White
    val ErrorContainer = Color(0xFFFFDAD6)
    val OnErrorContainer = Color(0xFF410002)

    val HighPriority = Color(0xFFE57373)
    val MediumPriority = Color(0xFFFFB74D)
    val LowPriority = Color(0xFF81C784)
    val Completed = Color(0xFF9E9E9E)
}

private val LightColorScheme = lightColorScheme(
    primary = AppColors.LightPrimary,
    onPrimary = AppColors.LightOnPrimary,
    primaryContainer = AppColors.LightPrimaryContainer,
    onPrimaryContainer = AppColors.LightOnPrimaryContainer,
    secondary = AppColors.LightSecondary,
    onSecondary = AppColors.LightOnSecondary,
    secondaryContainer = AppColors.LightSecondaryContainer,
    onSecondaryContainer = AppColors.LightOnSecondaryContainer,
    background = AppColors.LightBackground,
    onBackground = AppColors.LightOnBackground,
    surface = AppColors.LightSurface,
    onSurface = AppColors.LightOnSurface,
    surfaceVariant = AppColors.LightSurfaceVariant,
    onSurfaceVariant = AppColors.LightOnSurfaceVariant,
    outline = AppColors.LightOutline,
    error = AppColors.Error,
    onError = AppColors.OnError,
    errorContainer = AppColors.ErrorContainer,
    onErrorContainer = AppColors.OnErrorContainer,
)

private val DarkColorScheme = darkColorScheme(
    primary = AppColors.DarkPrimary,
    onPrimary = AppColors.DarkOnPrimary,
    primaryContainer = AppColors.DarkPrimaryContainer,
    onPrimaryContainer = AppColors.DarkOnPrimaryContainer,
    secondary = AppColors.DarkSecondary,
    onSecondary = AppColors.DarkOnSecondary,
    secondaryContainer = AppColors.DarkSecondaryContainer,
    onSecondaryContainer = AppColors.DarkOnSecondaryContainer,
    background = AppColors.DarkBackground,
    onBackground = AppColors.DarkOnBackground,
    surface = AppColors.DarkSurface,
    onSurface = AppColors.DarkOnSurface,
    surfaceVariant = AppColors.DarkSurfaceVariant,
    onSurfaceVariant = AppColors.DarkOnSurfaceVariant,
    outline = AppColors.DarkOutline,
    error = AppColors.Error,
    onError = AppColors.OnError,
    errorContainer = AppColors.ErrorContainer,
    onErrorContainer = AppColors.OnErrorContainer,
)

val AppShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp)
)

val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp
    )
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}