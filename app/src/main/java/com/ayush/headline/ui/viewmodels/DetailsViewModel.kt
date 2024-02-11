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
class DetailsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _saveArticleState = MutableLiveData<Response<Boolean>>(Response.None)
    val saveArticleState: LiveData<Response<Boolean>> get() = _saveArticleState

    private val _deleteArticleState = MutableLiveData<Response<Boolean>>(Response.None)
    val deleteArticleState: LiveData<Response<Boolean>> get() = _deleteArticleState

    fun saveArticle(likedArticle: LikedArticle) {
        viewModelScope.launch {
            newsRepository.saveArticle(likedArticle).collect {
                _saveArticleState.value = it
            }
        }
    }

    fun deleteArticle(likedArticle: LikedArticle) {
        viewModelScope.launch {
            newsRepository.deleteSavedArticle(likedArticle).collect {
                _deleteArticleState.value = it
            }
        }
    }

}