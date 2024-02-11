package com.ayush.headline.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.headline.data.models.LikedArticle
import com.ayush.headline.data.repository.NewsRepository
import com.ayush.headline.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {


    private val _getSavedArticlesState =
        MutableLiveData<Response<List<LikedArticle>>>(Response.None)
    val getSavedArticlesState: LiveData<Response<List<LikedArticle>>> get() = _getSavedArticlesState

    fun getSavedArticles() {
        viewModelScope.launch {
            newsRepository.getSavedArticles().collect {
                _getSavedArticlesState.value = it
            }
        }
    }

    init {
        getSavedArticles()
    }
}