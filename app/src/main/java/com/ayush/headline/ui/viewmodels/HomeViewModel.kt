package com.ayush.headline.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.headline.data.models.Article
import com.ayush.headline.data.models.NewsItem
import com.ayush.headline.data.repository.NewsRepository
import com.ayush.headline.utils.Constants
import com.ayush.headline.utils.PreferenceManager
import com.ayush.headline.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val newsRepository: NewsRepository
): ViewModel() {

    private val _topHeadlinesState = MutableLiveData<Response<NewsItem?>>(Response.None)
    val topHeadlinesState: LiveData<Response<NewsItem?>> get() = _topHeadlinesState

    private val _topHeadlinesByCategoryState = MutableLiveData<Response<NewsItem?>>(Response.None)
    val topHeadlinesByCategoryState: LiveData<Response<NewsItem?>> get() = _topHeadlinesByCategoryState

    private val _articlesFromDb = MutableLiveData<Response<List<Article>>>(Response.None)
    val articlesFromDb: LiveData<Response<List<Article>>> get() = _articlesFromDb

    init {
        getHeadlines()
    }

    fun userOnboarded() {
        viewModelScope.launch {
            preferenceManager.saveBooleanValue(Constants.FIRST, true)
        }
    }

    fun getHeadlines() {
        viewModelScope.launch {
            newsRepository.getTopHeadlines().collect {
                _topHeadlinesState.value = it
            }
        }
    }

    fun getHeadlinesByCategory(category: String) {
        viewModelScope.launch {
            newsRepository.getHeadlinesByCategory(category)
                .collect {
                    _topHeadlinesByCategoryState.value = it
                }
        }
    }

    fun fetchHeadlinesFromDb() {
        viewModelScope.launch {
            newsRepository.fetchDataFromDb().collect {
                _articlesFromDb.value = it
            }
        }
    }
}