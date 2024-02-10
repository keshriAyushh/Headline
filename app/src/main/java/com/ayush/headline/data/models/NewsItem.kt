package com.ayush.headline.data.models

data class NewsItem(
    val articles: List<Article>,
    val status: String?,
    val totalResults: Int?,
)