package com.ayush.headline.utils

sealed class Response<out T> {
    data object Loading: Response<Nothing>()
    data class Success<out T>(val data: T): Response<T>()
    data class Error(
        val message: String
    ): Response<Nothing>()
    data object None: Response<Nothing>()
}