package com.example.qiitaclient.presentation.viewmodel

import androidx.lifecycle.*
import com.example.qiitaclient.domain.model.ArticleWithTag
import com.example.qiitaclient.domain.repository.ArticleListRepository
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class ArticleListViewModel(
    private val articleRepository: ArticleListRepository
) : ViewModel() {

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val page = AtomicInteger(1)
    private val loadMore = AtomicBoolean(false)

    val articleList: LiveData<List<ArticleWithTag>> = articleRepository.getArticleList()

    init {
        refresh(page.get())
    }

    fun onRefresh() {
        _refreshing.value = true
        refresh(page.get())
    }

    fun onConsumeError() {
        _isError.value = false
    }

    fun onScrollEnd() {
        if (loadMore.compareAndSet(false, true)) {
            refresh(page.incrementAndGet())
        }
    }

    private fun refresh(page: Int) {
        viewModelScope.launch {
            runCatching {
                articleRepository.refreshArticleList(page)
            }.onFailure {
                _isError.value = true
            }
            _isLoading.value = false
            _refreshing.value = false
            loadMore.set(false)
        }
    }

    companion object {
        class Factory(
            private val articleRepository: ArticleListRepository
        ) : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) = ArticleListViewModel(
                articleRepository,
            ) as T
        }
    }
}
