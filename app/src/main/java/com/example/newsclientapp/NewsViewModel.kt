package com.example.newsclientapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsclientapp.model.ArticleArticle
import com.example.newsclientapp.network.NewsApiInterface
import com.example.newsclientapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface NewsUiState {
    object Loading : NewsUiState
    data class Success(val news: List<ArticleArticle>) : NewsUiState
    data class Error(val message: String) : NewsUiState
}

class NewsViewModel : ViewModel() {
    private val apiService = RetrofitClient.getInstance().create(NewsApiInterface::class.java)
    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()
    private val allArticles = mutableListOf<ArticleArticle>()
    private var currentOffset = 0
    private var isLoadingMore = false
    private val limit = 25

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        if (isLoadingMore) return
        isLoadingMore = true
        if (allArticles.isEmpty()) {
            _uiState.value = NewsUiState.Loading
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getTopNews(limit = limit, offset = currentOffset)
                if (response.isSuccessful && response.body() != null) {
                    val newNews = response.body()!!.results
                    allArticles.addAll(newNews)
                    _uiState.value = NewsUiState.Success(allArticles.toList())
                    currentOffset += limit
                } else {
                    _uiState.value = NewsUiState.Error("Ошибка сервера")
                }
            } catch (e: Exception) {
                _uiState.value = NewsUiState.Error("Сбой сети: ${e.localizedMessage}")
            } finally {
                isLoadingMore = false
            }
        }
    }
}