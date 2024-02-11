package com.ayush.headline.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ayush.headline.data.models.Article
import com.ayush.headline.data.models.LikedArticle

@Dao
interface ArticlesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<Article>)

    @Query("SELECT * FROM `article`")
    suspend fun getAllArticles(): List<Article>

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM `article`")
    suspend fun deleteAllArticles()

    @Query("SELECT * FROM `liked`")
    suspend fun getSavedArticles(): List<LikedArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(likedArticle: LikedArticle)

    @Delete
    suspend fun deleteSavedArticle(likedArticle: LikedArticle)

}