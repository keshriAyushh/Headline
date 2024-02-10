package com.ayush.headline.data.remote

import com.ayush.headline.data.models.NewsItem
import com.ayush.headline.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("top-headlines")
    suspend fun getHeadlines(
        @Query("country") country: String = "in",
        @Query("apiKey") apiKey: String = Constants.API_KEY,
        @Query("sortBy") sortBy: String = "relevancy",
        @Query("language") language: String = "en",
        @Query("pageSize") pageSize: Int = 100,
        @Query("category") category: String = ""
    ): Response<NewsItem>

    @GET("top-headlines")
    suspend fun getHeadlinesByCategory(
        @Query("country") country: String = "in",
        @Query("apiKey") apiKey: String = Constants.API_KEY,
        @Query("sortBy") sortBy: String = "relevancy",
        @Query("language") language: String = "en",
        @Query("pageSize") pageSize: Int = 100,
        @Query("category") category: String
    ): Response<NewsItem>


    @GET("everything")
    suspend fun getNewsByQuery(
        @Query("q") q: String,
        @Query("sortBy") sortBy: String = "relevancy",
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsItem>

}