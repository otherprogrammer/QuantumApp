package com.example.quantumapp.ui.components

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Importa Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.quantumapp.R

@OptIn(UnstableApi::class) // Necesario para usar PlayerView y otras APIs inestables de media3
@Composable
fun AnimatedCyberpunkVideoBackground(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Crea y recuerda una instancia de ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // Prepara el MediaItem desde el recurso raw
            val videoUri = Uri.parse("android.resource://${context.packageName}/${R.raw.cyberpunk_background}")
            setMediaItem(MediaItem.fromUri(videoUri))
            repeatMode = Player.REPEAT_MODE_ONE // Reproduce el video en bucle
            volume = 0f // Silencia el video para que no tenga audio de fondo
            playWhenReady = true // Empieza a reproducir automáticamente
            prepare() // Prepara el reproductor
        }
    }

    // Usa DisposableEffect para gestionar el ciclo de vida del ExoPlayer
    DisposableEffect(
        key1 = exoPlayer, // Añade exoPlayer como key para que el efecto se disponga y recree si el reproductor cambia
        effect = {
            onDispose {
                // Libera los recursos del ExoPlayer cuando el composable se descompone
                exoPlayer.release()
            }
        }
    )

    // Un Box que actuará como contenedor y proporcionará el color de fondo de cobertura
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF000000)) // Color de fondo oscuro púrpura/azul que se mezcla con el video
    ) {
        // El AndroidView que contiene el PlayerView
        AndroidView(
            modifier = Modifier.fillMaxSize(), // El PlayerView intenta llenar todo el Box
            factory = {
                // Crea una PlayerView para mostrar el video
                PlayerView(it).apply {
                    layoutParams = android.view.ViewGroup.LayoutParams( // Asegura que el PlayerView ocupe todo el espacio de su padre
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    useController = false // No mostrar los controles de reproducción
                    player = exoPlayer // Asigna el ExoPlayer a la PlayerView
                    setBackgroundColor(android.graphics.Color.TRANSPARENT) // Hace el fondo del PlayerView transparente para que el video se vea
                }
            }
        )
    }
}
