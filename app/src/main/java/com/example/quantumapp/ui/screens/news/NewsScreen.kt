package com.example.quantumapp.ui.screens.news

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.quantumapp.model.NewsArticle
import com.example.quantumapp.ui.components.AnimatedCyberpunkVideoBackground
import com.example.quantumapp.viewmodel.NewsState
import com.example.quantumapp.viewmodel.NewsViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    navController: NavController,
    newsViewModel: NewsViewModel = viewModel()
) {
    val newsState by newsViewModel.newsState.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedCyberpunkVideoBackground(modifier = Modifier.fillMaxSize())

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Noticias Cuánticas",
                            color = Color(0xFF00FFFF),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold // Añade negrita para más impacto
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color(0xFF00FFFF)
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (newsState) {
                    is NewsState.Loading -> {
                        CircularProgressIndicator(color = Color(0xFF00FFFF))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Cargando noticias cuánticas...",
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    is NewsState.Success -> {
                        val articles = (newsState as NewsState.Success).articles
                        if (articles.isEmpty()) {
                            Text(
                                text = "No se encontraron noticias. Intenta más tarde.",
                                color = Color.LightGray,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(articles) { article ->
                                    // Envuelve la tarjeta en AnimatedVisibility para animaciones de entrada
                                    AnimatedVisibility(
                                        visible = true, // Siempre visible una vez cargado
                                        enter = slideInVertically(animationSpec = tween(durationMillis = 500)) { it / 2 } + fadeIn(animationSpec = tween(durationMillis = 500))
                                    ) {
                                        NewsArticleCard(article = article) {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                                            context.startActivity(intent)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    is NewsState.Error -> {
                        val errorMessage = (newsState as NewsState.Error).message
                        Text(
                            text = "Error: $errorMessage",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { newsViewModel.fetchNewsArticles() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsArticleCard(article: NewsArticle, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A2E).copy(alpha = 0.7f),
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            article.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = article.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00FFFF),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Limpia las etiquetas HTML de la descripción
            val cleanDescription = article.description?.let {
                it.replace(Regex("<.*?>"), "") // Elimina todas las etiquetas HTML
                    .replace("&nbsp;", " ") // Reemplaza entidades HTML comunes
                    .trim() // Elimina espacios en blanco al inicio/fin
            }

            cleanDescription?.let { description ->
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp)) // Aumenta el espaciado
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Formatea la fecha de publicación
                article.pubDate?.let { pubDate ->
                    val formattedDate = try {
                        // Intenta parsear varios formatos comunes de fecha RSS
                        val parser = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
                        val date = parser.parse(pubDate)
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
                    } catch (e: Exception) {
                        pubDate // Si falla el parseo, usa la fecha original
                    }
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Text(
                    text = article.source,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF00FFFF).copy(alpha = 0.8f)
                )
            }
        }
    }
}