package com.example.quantumapp.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedGateChip(label: String, onClick: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    var scale by remember { mutableStateOf(0.8f) }

    LaunchedEffect(Unit) {
        visible = true
        animate(
            initialValue = 0.8f,
            targetValue = 1f,
            animationSpec = tween(500)
        ) { value, _ -> scale = value }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)) + scaleIn(tween(400)),
        exit = fadeOut(tween(200))
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = Color(0xFF1F1F3F),
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .clickable { onClick() }
                .padding(4.dp),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Text(
                text = label,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = Color(0xFF00FFFF),
                style = MaterialTheme.typography.bodyLarge.copy(
                    shadow = Shadow(Color.Cyan, Offset(1f, 1f), 8f)
                )
            )
        }
    }
}
