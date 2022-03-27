package com.example.qiitaclient.presentation.viewmodel

import androidx.lifecycle.*
import com.example.qiitaclient.domain.model.ArticleWithTag
import com.example.qiitaclient.domain.repository.ArticleListRepository
import kotlinx.coroutines.launch

class ArticleListViewModel(
    private val articleRepository: ArticleListRepository
) : ViewModel() {

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    val articleList: LiveData<List<ArticleWithTag>> = articleRepository.getArticleList()

    init {
        refresh()
    }

    fun onRefresh() {
        _refreshing.value = true
        refresh()
    }

    fun onConsumeError() {
        _isError.value = false
    }

    private fun refresh() {
        viewModelScope.launch {
            runCatching {
                articleRepository.refreshArticleList()
            }.onSuccess {
                _isLoading.value = false
                _refreshing.value = false
            }.onFailure {
                _isError.value = true
            }
        }
    }

    companion object {
        class Factory(
            private val articleRepository: ArticleListRepository
        ) : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>) = ArticleListViewModel(
                articleRepository,
            ) as T
        }
    }
}
