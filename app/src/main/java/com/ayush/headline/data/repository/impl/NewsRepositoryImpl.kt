package com.ayush.headline.data.repository.impl

import com.ayush.headline.data.models.NewsItem
import com.ayush.headline.data.remote.NewsService
import com.ayush.headline.data.repository.NewsRepository
import com.ayush.headline.utils.Constants
import com.ayush.headline.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsService
): NewsRepository {

    override fun getTopHeadlines(): Flow<Response<NewsItem?>> = flow {
        try {
            emit(Response.None)
            emit(Response.Loading)
            val apiCall = newsApi.getHeadlines()
            if(apiCall.isSuccessful) {
                emit(Response.Success(apiCall.body()))
            } else {
                emit(Response.Success(null))
            }
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: Constants.ERR))
        }
    }

    override fun getHeadlinesByCategory(category: String): Flow<Response<NewsItem?>> = flow {
        try {
            emit(Response.None)
            emit(Response.Loading)
            val apiCall = newsApi.getHeadlinesByCategory(category = category)
            if(apiCall.isSuccessful) {
                emit(Response.Success(apiCall.body()))
            } else {
                emit(Response.Success(null))
            }
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: Constants.ERR))
        }
    }

    override fun getNewsByQuery(query: String): Flow<Response<NewsItem?>> = flow {
        try {
            emit(Response.None)
            emit(Response.Loading)
            val apiCall = newsApi.getNewsByQuery(q = query)
            if(apiCall.isSuccessful) {
                emit(Response.Success(apiCall.body()))
            } else {
                emit(Response.Success(null))
            }
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: Constants.ERR))
        }
    }
}