package com.ayush.headline.data.models

import androidx.room.Entity

@Entity("article")
data class Article(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String,
    val isLiked: Boolean
)