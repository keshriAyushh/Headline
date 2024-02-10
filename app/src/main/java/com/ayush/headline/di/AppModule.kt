package com.ayush.headline.di

import android.app.Application
import androidx.room.Room
import com.ayush.headline.data.local.ArticleDatabase
import com.ayush.headline.data.remote.NewsService
import com.ayush.headline.data.repository.NewsRepository
import com.ayush.headline.data.repository.impl.NewsRepositoryImpl
import com.ayush.headline.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesNewsApi() = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsService::class.java)

    @Provides
    @Singleton
    fun providesArticlesDatabase(app: Application): ArticleDatabase {
        return Room.databaseBuilder(
            app.applicationContext,
            ArticleDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providesNewsRepository(newsService: NewsService, db: ArticleDatabase): NewsRepository = NewsRepositoryImpl(newsService, db)
}