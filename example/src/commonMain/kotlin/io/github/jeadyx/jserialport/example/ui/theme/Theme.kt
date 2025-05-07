package io.github.jeadyx.jserialport.example.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White

@Composable
fun JSerialPortTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = lightColors(
        primary = Color(0xFF1976D2),
        secondary = Color(0xFF4CAF50),
        background = Color(0xfff0f0ff),
        surface = Color(0xfff0f0f0),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color.Black,
        onSurface = Color.Black,
        error = Color.Red,
        onError = Color.Red
    )
    MaterialTheme(
        colors = colorScheme,
        content = content
    )
} 