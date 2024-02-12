package com.ayush.headline.utils
/*
    Wrapping the states of the application when some action is performed
    Response encapsulates different states of response, and this Response class is
    emitted by flow and collected by viewmodel.
 */
sealed class Response<out T> {
    data object Loading: Response<Nothing>()
    data class Success<out T>(val data: T): Response<T>()
    data class Error(
        val message: String
    ): Response<Nothing>()
    data object None: Response<Nothing>()
}