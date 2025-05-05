package com.example.quantumapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val QuantumDarkColorScheme = darkColorScheme(
    primary = Color(0xFF253745),
    onPrimary = Color(0xFF9BA8AB),
    secondary = Color(0xFF4A5C6A),
    background = Color(0xFF06141B),
    surface = Color(0xFF11212D),
    onBackground = Color(0xFFCCD0CF),
    onSurface = Color(0xFF9BA8AB)
)

@Composable
fun QuantumAppTheme(
    darkTheme: Boolean = true, // puedes personalizar esto si tienes modo claro también
    content: @Composable () -> Unit
) {
    val colorScheme = QuantumDarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
