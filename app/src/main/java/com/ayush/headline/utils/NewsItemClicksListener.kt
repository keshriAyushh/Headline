package com.ayush.headline.utils

import com.ayush.headline.data.models.Article

interface NewsItemClicksListener {
    fun onItemClicked(url: String, article: Article)
}