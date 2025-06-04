package com.example.quantumapp.data.news

import android.util.Log
import com.example.quantumapp.model.NewsArticle
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.util.*
import kotlinx.coroutines.Dispatchers // ¡Importa esto!
import kotlinx.coroutines.withContext // ¡Importa esto!

/**
 * Clase para obtener y parsear feeds RSS de noticias.
 */
class RssParser {

    private val client = OkHttpClient()
    private val TAG = "RssParser" // Etiqueta para los logs

    /**
     * Obtiene un feed RSS de la URL proporcionada y lo parsea en una lista de NewsArticle.
     *
     * @param url La URL del feed RSS.
     * @param sourceName El nombre de la fuente de la noticia para cada artículo.
     * @return Una lista de NewsArticle, o una lista vacía si falla.
     */
    suspend fun fetchRssFeed(url: String, sourceName: String): List<NewsArticle> {
        Log.d(TAG, "Intentando obtener feed RSS de: $url (Fuente: $sourceName)")
        return try {
            // ¡LA CLAVE ESTÁ AQUÍ! Ejecuta la operación de red bloqueante en el Dispatchers.IO
            val response = withContext(Dispatchers.IO) {
                val request = Request.Builder().url(url).build()
                client.newCall(request).execute() // Esta línea es la que debe estar en el hilo de fondo
            }

            if (response.isSuccessful) {
                val xmlString = response.body?.string()
                if (xmlString != null) {
                    Log.d(TAG, "Feed RSS obtenido exitosamente de $url. Tamaño: ${xmlString.length} caracteres.")
                    // El parseo XML también puede ser intensivo, así que es buena práctica mantenerlo en IO
                    withContext(Dispatchers.IO) {
                        parseRssXml(xmlString, sourceName)
                    }
                } else {
                    Log.w(TAG, "Respuesta exitosa de $url pero cuerpo vacío.")
                    emptyList()
                }
            } else {
                Log.e(TAG, "Error al obtener el feed RSS de $url. Código: ${response.code}, Mensaje: ${response.message}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener o procesar el feed RSS de $url: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * Parsea la cadena XML de un feed RSS en una lista de NewsArticle.
     *
     * @param xmlString La cadena XML del feed RSS.
     * @param sourceName El nombre de la fuente para asignar a los artículos.
     * @return Una lista de NewsArticle.
     */
    private fun parseRssXml(xmlString: String, sourceName: String): List<NewsArticle> {
        val articles = mutableListOf<NewsArticle>()
        var parser: XmlPullParser? = null
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = false
            parser = factory.newPullParser()
            parser.setInput(StringReader(xmlString))

            var eventType = parser.eventType
            var currentTitle: String? = null
            var currentLink: String? = null
            var currentDescription: String? = null
            var currentPubDate: String? = null
            var currentImageUrl: String? = null
            var inItem = false

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name?.toLowerCase(Locale.ROOT)

                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "item") {
                            inItem = true
                            currentTitle = null
                            currentLink = null
                            currentDescription = null
                            currentPubDate = null
                            currentImageUrl = null
                        } else if (inItem) {
                            when (tagName) {
                                "title" -> {
                                    parser.next()
                                    currentTitle = parser.text?.trim()
                                }
                                "link" -> {
                                    parser.next()
                                    currentLink = parser.text?.trim()
                                }
                                "description" -> {
                                    parser.next()
                                    currentDescription = parser.text?.trim()
                                }
                                "content:encoded" -> {
                                    parser.next()
                                    if (currentDescription == null) {
                                        currentDescription = parser.text?.trim()
                                    }
                                }
                                "pubdate", "dc:date" -> {
                                    parser.next()
                                    currentPubDate = parser.text?.trim()
                                }
                                "enclosure" -> {
                                    val url = parser.getAttributeValue(null, "url")
                                    if (url != null && (url.endsWith(".jpg", true) || url.endsWith(".png", true) || url.endsWith(".jpeg", true) || url.endsWith(".webp", true))) {
                                        currentImageUrl = url
                                    }
                                }
                                "media:content" -> {
                                    val url = parser.getAttributeValue(null, "url")
                                    if (url != null && (url.endsWith(".jpg", true) || url.endsWith(".png", true) || url.endsWith(".jpeg", true) || url.endsWith(".webp", true))) {
                                        currentImageUrl = url
                                    }
                                }
                                "media:thumbnail" -> {
                                    val url = parser.getAttributeValue(null, "url")
                                    if (url != null && (url.endsWith(".jpg", true) || url.endsWith(".png", true) || url.endsWith(".jpeg", true) || url.endsWith(".webp", true))) {
                                        if (currentImageUrl == null) {
                                            currentImageUrl = url
                                        }
                                    }
                                }
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (tagName == "item") {
                            inItem = false
                            if (currentTitle != null && currentLink != null) {
                                val newArticle = NewsArticle(
                                    title = currentTitle!!,
                                    link = currentLink!!,
                                    description = currentDescription,
                                    pubDate = currentPubDate,
                                    imageUrl = currentImageUrl,
                                    source = sourceName
                                )
                                articles.add(newArticle)
                                Log.d(TAG, "Artículo parseado: ${newArticle.title} de ${newArticle.source}")
                            } else {
                                Log.w(TAG, "Artículo incompleto encontrado (falta título o enlace) en $sourceName. Título: $currentTitle, Enlace: $currentLink")
                            }
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción durante el parseo del XML RSS de $sourceName: ${e.message}", e)
        }
        Log.d(TAG, "Parseo completado para $sourceName. Artículos encontrados: ${articles.size}")
        return articles
    }
}