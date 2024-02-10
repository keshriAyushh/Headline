package com.ayush.headline.data.repository

import com.ayush.headline.data.models.Article
import com.ayush.headline.data.models.NewsItem
import com.ayush.headline.utils.Response
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getTopHeadlines(): Flow<Response<NewsItem?>>
    fun getHeadlinesByCategory(category: String): Flow<Response<NewsItem?>>
    fun getNewsByQuery(query: String): Flow<Response<NewsItem?>>
    fun fetchDataFromDb(): Flow<Response<List<Article>>>
}