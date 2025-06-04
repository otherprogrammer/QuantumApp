package com.example.quantumapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quantumapp.data.news.RssParser
import com.example.quantumapp.model.NewsArticle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el estado y la lógica de la sección de noticias.
 */
class NewsViewModel : ViewModel() {

    // Representa el estado de la carga de noticias
    private val _newsState = MutableStateFlow<NewsState>(NewsState.Loading)
    val newsState: StateFlow<NewsState> = _newsState

    private val rssParser = RssParser()

    // Lista de URLs de feeds RSS y sus nombres de fuente.
    // Puedes añadir más feeds aquí. Es crucial verificar que estas URLs sean válidas y activas.
    private val rssFeedUrls = listOf(
        "https://quantumcomputingreport.com/feed/" to "Quantum Computing Report", // Ejemplo, verifica la URL real
        // ¡IMPORTANTE! Reemplaza o añade URLs de feeds RSS reales y válidos de computación cuántica.
        // Puedes buscar "quantum computing RSS feed" en Google para encontrar más.
    )

    init {
        fetchNewsArticles()
    }

    /**
     * Obtiene los artículos de noticias de todas las fuentes RSS configuradas.
     */
    fun fetchNewsArticles() {
        _newsState.value = NewsState.Loading // Establece el estado a cargando
        viewModelScope.launch {
            try {
                val allArticles = mutableListOf<NewsArticle>()
                for ((url, sourceName) in rssFeedUrls) {
                    val articlesFromSource = rssParser.fetchRssFeed(url, sourceName)
                    allArticles.addAll(articlesFromSource)
                }
                // Ordena los artículos por fecha de publicación (si la fecha es parseable)
                // Esto es una simplificación, para un manejo robusto de fechas, se necesitaría un parser de fecha.
                val sortedArticles = allArticles.sortedByDescending { it.pubDate } // Ordena por fecha de publicación descendente
                _newsState.value = NewsState.Success(sortedArticles) // Actualiza con los artículos cargados
            } catch (e: Exception) {
                e.printStackTrace()
                _newsState.value = NewsState.Error("Error al cargar las noticias: ${e.message}") // Establece el estado a error
            }
        }
    }
}

/**
 * Sellada para representar los diferentes estados de la UI de noticias.
 */
sealed class NewsState {
    object Loading : NewsState() // Estado de carga
    data class Success(val articles: List<NewsArticle>) : NewsState() // Carga exitosa con datos
    data class Error(val message: String) : NewsState() // Error durante la carga
}
