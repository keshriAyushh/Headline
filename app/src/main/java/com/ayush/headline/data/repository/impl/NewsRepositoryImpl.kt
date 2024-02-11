package com.ayush.headline.data.repository.impl

import com.ayush.headline.data.local.ArticleDatabase
import com.ayush.headline.data.models.Article
import com.ayush.headline.data.models.LikedArticle
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
    private val newsApi: NewsService,
    private val articleDatabase: ArticleDatabase
) : NewsRepository {

    override fun getTopHeadlines(): Flow<Response<NewsItem?>> = flow {
        try {
            emit(Response.None)
            emit(Response.Loading)
            val apiCall = newsApi.getHeadlines()
            if (apiCall.isSuccessful) {
                val data = apiCall.body()
                emit(Response.Success(data)) //Emitting the data to the viewmodel
                articleDatabase.articlesDao.deleteAllArticles() //Clear the database to avoid bulking
                articleDatabase.articlesDao.insertArticles(
                    data?.articles?.subList(0, 20) ?: emptyList()
                ) //Caching the articles
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
            if (apiCall.isSuccessful) {
                val data = apiCall.body()
                articleDatabase.articlesDao.deleteAllArticles()
                articleDatabase.articlesDao.insertArticles(
                    data?.articles?.subList(0, 20) ?: emptyList()
                )
                emit(Response.Success(data))
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
            if (apiCall.isSuccessful) {
                val data = apiCall.body()
                articleDatabase.articlesDao.deleteAllArticles()
                articleDatabase.articlesDao.insertArticles(
                    data?.articles?.subList(0, 20) ?: emptyList()
                )
                emit(Response.Success(data))
            } else {
                emit(Response.Success(null))
            }
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: Constants.ERR))
        }
    }

    override fun saveArticle(likedArticle: LikedArticle): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.None)
            emit(Response.Loading)
            articleDatabase.articlesDao.saveArticle(likedArticle)
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: Constants.ERR))
        }
    }

    override fun getSavedArticles(): Flow<Response<List<LikedArticle>>> = flow {
        try {
            emit(Response.None)
            emit(Response.Loading)
            val likedArticles = articleDatabase.articlesDao.getSavedArticles()
            emit(Response.Success(likedArticles))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: Constants.ERR))
        }
    }

    override fun deleteSavedArticle(likedArticle: LikedArticle): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.None)
            emit(Response.Loading)
            articleDatabase.articlesDao.deleteSavedArticle(likedArticle)
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: Constants.ERR))
        }
    }

    override fun fetchDataFromDb(): Flow<Response<List<Article>>> = flow {
        try {
            emit(Response.None)
            emit(Response.Loading)
            val data = articleDatabase.articlesDao.getAllArticles()
            emit(Response.Success(data))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: Constants.ERR))
        }
    }
}