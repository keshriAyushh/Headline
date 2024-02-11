package com.ayush.headline.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ayush.headline.data.models.Article
import com.ayush.headline.data.models.LikedArticle

@Database(entities = [Article::class, LikedArticle::class], version = 1)
@TypeConverters(SourceConverter::class)
abstract class ArticleDatabase: RoomDatabase() {
    abstract val articlesDao: ArticlesDao
}