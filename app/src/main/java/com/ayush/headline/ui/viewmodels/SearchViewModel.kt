package com.ayush.headline.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.headline.data.models.NewsItem
import com.ayush.headline.data.repository.NewsRepository
import com.ayush.headline.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {

    private val _newsByQueryState = MutableLiveData<Response<NewsItem?>>(Response.None)
    val newsByQueryState get() = _newsByQueryState

    fun getNewsByQuery(query: String) {
        viewModelScope.launch {
            newsRepository.getNewsByQuery(query).collect {
                _newsByQueryState.value = it
            }
        }
    }
}