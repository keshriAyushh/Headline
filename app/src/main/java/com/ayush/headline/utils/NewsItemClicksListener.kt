package com.ayush.headline.utils

import com.ayush.headline.data.models.Article
import com.ayush.headline.data.models.LikedArticle

interface NewsItemClicksListener {
    fun onItemClicked(article: Article, likedArticle: LikedArticle)
}