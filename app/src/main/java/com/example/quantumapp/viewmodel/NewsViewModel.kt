package com.example.quantumapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quantumapp.data.news.RssParser
import com.example.quantumapp.model.NewsArticle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.json.JSONArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Sellada para representar los diferentes estados de la UI de noticias.
 */
sealed class NewsState {
    object Loading : NewsState() // Estado de carga
    data class Success(val articles: List<NewsArticle>) : NewsState() // Carga exitosa con datos
    data class Error(val message: String) : NewsState() // Error durante la carga
}

/**
 * ViewModel para gestionar el estado y la lógica de la sección de noticias.
 */
class NewsViewModel : ViewModel() {

    private val TAG = "NewsViewModel"

    private val _newsState = MutableStateFlow<NewsState>(NewsState.Loading)
    val newsState: StateFlow<NewsState> = _newsState

    private val rssParser = RssParser()
    private val httpClient = OkHttpClient() // Cliente HTTP para la API de Gemini

    // Lista de URLs de feeds RSS y sus nombres de fuente.
    private val rssFeedUrls = listOf(
        "https://www.ibm.com/blogs/research/category/quantum-computing/feed/" to "IBM Quantum Blog",
        "https://spectrum.ieee.org/tag/quantum-computing.rss" to "IEEE Spectrum Quantum",
        "https://www.quantamagazine.org/tag/quantum-computing/feed/" to "Quanta Magazine Quantum",
        "https://quantumcomputingreport.com/feed/" to "Quantum Computing Report"
    )

    init {
        fetchNewsArticles()
    }

    /**
     * Obtiene los artículos de noticias de todas las fuentes RSS configuradas y los traduce.
     */
    fun fetchNewsArticles() {
        _newsState.value = NewsState.Loading
        Log.d(TAG, "Iniciando la carga y traducción de noticias...")
        viewModelScope.launch {
            try {
                val allArticles = mutableListOf<NewsArticle>()
                for ((url, sourceName) in rssFeedUrls) {
                    Log.d(TAG, "Intentando obtener artículos de $sourceName ($url)")
                    val articlesFromSource = rssParser.fetchRssFeed(url, sourceName)
                    allArticles.addAll(articlesFromSource)
                    Log.d(TAG, "Se obtuvieron ${articlesFromSource.size} artículos de $sourceName.")
                }

                // Traducir los artículos
                val translatedArticles = allArticles.map { article ->
                    // Crear una copia mutable para actualizar los campos de traducción
                    val mutableArticle = article.copy()

                    // Traducir título
                    if (article.title.isNotBlank() && article.title.length > 2) {
                        mutableArticle.translatedTitle = translateText(article.title, "es")
                    } else {
                        mutableArticle.translatedTitle = article.title
                    }


                    // Traducir descripción (solo si existe y es lo suficientemente larga)
                    if (article.description != null && article.description.isNotBlank() && article.description.length > 10) {
                        val cleanDescription = article.description.replace(Regex("<.*?>"), "").replace("&nbsp;", " ").trim()
                        mutableArticle.translatedDescription = translateText(cleanDescription, "es")
                    } else {
                        mutableArticle.translatedDescription = article.description
                    }
                    mutableArticle
                }

                val sortedArticles = translatedArticles.sortedByDescending { it.pubDate }
                _newsState.value = NewsState.Success(sortedArticles)
                Log.d(TAG, "Carga y traducción de noticias completada. Total de artículos: ${sortedArticles.size}")
            } catch (e: Exception) {
                e.printStackTrace()
                _newsState.value = NewsState.Error("Error al cargar o traducir las noticias: ${e.message}")
                Log.e(TAG, "Error general al cargar o traducir noticias: ${e.message}", e)
            }
        }
    }

    /**
     * Traduce un texto usando la API de Gemini.
     * @param text El texto a traducir.
     * @param targetLanguage El código del idioma objetivo (ej. "es" para español).
     * @return El texto traducido, o el texto original si falla la traducción.
     */
    private suspend fun translateText(text: String, targetLanguage: String): String {
        return withContext(Dispatchers.IO) {
            try {
                if (text.isBlank() || text.length < 3) {
                    Log.d(TAG, "Saltando traducción para texto muy corto/vacío: '$text'")
                    return@withContext text
                }

                // --- MODIFICACIÓN CLAVE AQUÍ: Ajuste del prompt y post-procesamiento ---
                val prompt = "Translate the following text to $targetLanguage. Provide only the translation, without any introductory phrases or conversational text. Text to translate: $text"
                // --- FIN MODIFICACIÓN ---

                val chatHistory = JSONObject().put("role", "user").put("parts", JSONArray().put(JSONObject().put("text", prompt)))
                val payload = JSONObject().put("contents", JSONArray().put(chatHistory))

                val apiKey = "AIzaSyAdwUOfurBeU9bP_FO9NeYmP46bIY4qGHk"
                val apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey"

                val requestBody = payload.toString().toRequestBody("application/json".toMediaTypeOrNull())

                Log.d(TAG, "Enviando solicitud de traducción para: '$text'")
                Log.d(TAG, "URL de la API: $apiUrl")
                Log.d(TAG, "Payload de la solicitud: ${payload.toString()}")

                val request = Request.Builder()
                    .url(apiUrl)
                    .post(requestBody)
                    .build()

                val response = httpClient.newCall(request).execute()
                val responseBody = response.body?.string()

                Log.d(TAG, "Respuesta de la API de traducción (Código: ${response.code}): $responseBody")

                if (response.isSuccessful && responseBody != null) {
                    val jsonResponse = JSONObject(responseBody)
                    if (jsonResponse.has("error")) {
                        val errorMessage = jsonResponse.getJSONObject("error").optString("message", "Error desconocido de la API")
                        Log.e(TAG, "Error de la API de Gemini para '$text': $errorMessage")
                        return@withContext text
                    }

                    val candidates = jsonResponse.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val content = candidates.getJSONObject(0).optJSONObject("content")
                        if (content != null) {
                            val parts = content.optJSONArray("parts")
                            if (parts != null && parts.length() > 0) {
                                var translatedText = parts.getJSONObject(0).optString("text", text)

                                // --- MODIFICACIÓN CLAVE AQUÍ: Post-procesamiento para limpiar frases introductorias ---
                                val unwantedPhrases = listOf(
                                    "Here's the translation:",
                                    "Aquí tienes la traducción:",
                                    "Here are a few options, depending on the nuance y...",
                                    "Aquí tienes una traducción de ese texto al español:",
                                    "Aquí tienes una traducción:",
                                    "La traducción es:",
                                    "El texto traducido es:",
                                    "Traducción:",
                                    "Translated text:",
                                    "Aquí tienes la traducción del texto al español:",
                                    "Aquí tienes la traducción al español:",
                                    "La traducción al español es:",
                                    "Aquí hay algunas opciones, dependiendo del matiz y...",
                                    "Aquí hay algunas opciones, con ligeros matices diferentes:",
                                    "Aquí tienes la traducción del texto al español:",
                                    "Aquí hay algunas opciones:",
                                    "Aquí tienes la traducción del texto al español:"
                                )
                                for (phrase in unwantedPhrases) {
                                    translatedText = translatedText.replace(phrase, "", ignoreCase = true).trim()
                                }
                                // Eliminar cualquier "..." al inicio o final si queda
                                translatedText = translatedText.trimStart('.').trimEnd('.')
                                // Eliminar espacios extra
                                translatedText = translatedText.replace(Regex("\\s+"), " ").trim()
                                // --- FIN MODIFICACIÓN ---

                                Log.d(TAG, "Texto traducido exitosamente (limpio): '$text' -> '$translatedText'")
                                return@withContext translatedText
                            }
                        }
                    }
                }
                Log.e(TAG, "Fallo en la traducción de: '$text'. Respuesta: ${response.code} - $responseBody}")
                text
            } catch (e: Exception) {
                Log.e(TAG, "Excepción durante la traducción de: '$text': ${e.message}", e)
                text
            }
        }
    }
}