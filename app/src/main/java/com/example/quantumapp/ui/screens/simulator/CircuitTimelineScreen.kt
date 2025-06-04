package com.example.quantumapp.ui.screens.simulator

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun CircuitTimelineScreen(navController: NavController, circuitId: String) {
    val circuitTitle = if (circuitId.isNotEmpty()) "Circuito Actual" else "Nuevo Circuito"

    val steps = listOf(
        "Inicialización del Qubit",
        "Aplicación de Compuerta Hadamard",
        "Aplicación de Compuerta Pauli-X",
        "Medición del Qubit",
        "Resultado Final"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // 🎨 Fondo Cyberpunk
        CyberpunkBackground()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Timeline,
                contentDescription = "Timeline",
                tint = Color(0xFF00FFFF),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 🌈 Texto con efecto neón
            NeonText(text = "Línea de Tiempo del Circuito")

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A2E)
                ),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = circuitTitle,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF00FFFF)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Identificador: ${circuitId.take(6)}...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 💡 Línea de tiempo animada
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(steps.size) { index ->
                    TimelineStep(step = steps[index], index = index)
                }
            }
        }
    }
}

// 🎨 Fondo con partículas estilo Cyberpunk
@Composable
fun CyberpunkBackground(modifier: Modifier = Modifier) {
    val particleCount = 60
    val particles = remember {
        List(particleCount) {
            Offset(Random.nextFloat(), Random.nextFloat())
        }
    }

    val transition = rememberInfiniteTransition(label = "particleShift")
    val offsetAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(20000, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "offset"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        particles.forEach { base ->
            val x = (base.x * width + offsetAnim * 100f) % width
            val y = (base.y * height + offsetAnim * 200f) % height

            drawCircle(
                color = Color(0xFF00FFFF).copy(alpha = 0.15f),
                center = Offset(x, y),
                radius = (2..5).random().toFloat()
            )
        }
    }
}

// 🌈 Texto con sombra y neón
@Composable
fun NeonText(text: String) {
    Text(
        text = text,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF00FFFF),
        style = TextStyle(
            shadow = Shadow(
                color = Color.Cyan,
                blurRadius = 25f,
                offset = Offset(2f, 2f)
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, shape = RoundedCornerShape(8.dp))
    )
}

// 💡 Paso animado tipo cinematic
@Composable
fun TimelineStep(step: String, index: Int) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 250L)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(animationSpec = tween(500)) + fadeIn(tween(500)),
        exit = fadeOut()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0F0F23),
                contentColor = Color.White
            ),
            elevation = CardDefaults.elevatedCardElevation(6.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Paso",
                    tint = Color(0xFF00FFFF),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Paso ${index + 1}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF00FFFF)
                    )
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}
