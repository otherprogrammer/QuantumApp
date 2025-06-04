package com.example.quantumapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val QuantumDarkColorScheme = darkColorScheme(
    primary = Color(0xFF253745),
    onPrimary = Color(0xFF9BA8AB),
    secondary = Color(0xFF4A5C6A),
    background = Color.Transparent, // Correcto: transparente
    surface = Color.Transparent,    // Correcto: transparente
    onBackground = Color(0xFFCCD0CF),
    onSurface = Color(0xFF9BA8AB)
)

@Composable
fun QuantumAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // O false, dependiendo de tu configuración
    content: @Composable () -> Unit
) {
    val colorScheme = QuantumDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb() // Correcto: barra de estado transparente
            window.navigationBarColor = Color.Transparent.toArgb() // Correcto: barra de navegación transparente
            WindowCompat.setDecorFitsSystemWindows(window, false) // Correcto: permite dibujar detrás de las barras
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}