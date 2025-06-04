package com.example.quantumapp.model

/**
 * Clase de datos que representa un artículo de noticias obtenido de un feed RSS.
 *
 * @param title El título del artículo.
 * @param link La URL completa del artículo.
 * @param description Un breve resumen o descripción del artículo (puede ser nulo).
 * @param pubDate La fecha de publicación del artículo en formato de cadena (puede ser nulo).
 * @param imageUrl Una URL de imagen asociada al artículo (puede ser nulo, ya que no todos los RSS lo tienen).
 * @param source El nombre de la fuente de la noticia (ej. "IBM Quantum Blog").
 */
data class NewsArticle(
    val title: String,
    val link: String,
    val description: String?,
    val pubDate: String?,
    val imageUrl: String?,
    val source: String
)
