package com.ayush.headline.utils

object Constants {
    const val ERR: String = "An unknown error has occurred!"
    const val FIRST: String = "first"
    const val LOSING: String = "Poor Connection!"
    const val UNAVAILABLE: String = "No internet connection! Showing limited content"
    const val BASE_URL: String = "https://newsapi.org/v2/"
    const val API_KEY: String = "78ce5a32068c435faa5963affc586149"
    val categories = listOf(
        "General",
        "Business",
        "Politics",
        "Science",
        "Sports",
        "Health",
        "Entertainment",
        "Technology"
    )
}