package com.truevoice.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TrueVoiceDarkScheme = darkColorScheme(
    primary = Color(0xFF00C8FF),
    secondary = Color(0xFF7B61FF),
    tertiary = Color(0xFF00FF87),
    background = Color(0xFF050A14),
    surface = Color(0xFF080E1C),
    onPrimary = Color(0xFF050A14),
    onSecondary = Color(0xFF050A14),
    onBackground = Color(0xFFCCDFFF),
    onSurface = Color(0xFFCCDFFF),
    error = Color(0xFFFF3B5C)
)

@Composable
fun TrueVoiceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TrueVoiceDarkScheme,
        content = content
    )
}
