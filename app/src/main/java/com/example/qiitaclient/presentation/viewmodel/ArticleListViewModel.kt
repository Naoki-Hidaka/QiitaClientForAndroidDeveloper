package com.example.qiitaclient.presentation.viewmodel

import androidx.lifecycle.*
import com.example.qiitaclient.domain.model.ArticleWithTag
import com.example.qiitaclient.domain.repository.ArticleListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val articleRepository: ArticleListRepository
) : ViewModel() {

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val _onScrollTop = MutableLiveData(false)
    val onScrollTop: LiveData<Boolean> = _onScrollTop

    private val page = AtomicInteger(1)
    private val loadMore = AtomicBoolean(false)

    val articleList: LiveData<List<ArticleWithTag>> =
        articleRepository.getArticleList().map { articleList ->
            articleList.sortedByDescending { it.article.createdAt }
        }

    fun init() {
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

    fun onScrolledTop() {
        _onScrollTop.value = false
    }

    private fun refresh(page: Int) {
        viewModelScope.launch {
            runCatching {
                articleRepository.refreshArticleList(page)
            }.onSuccess {
                if (page == 0) {
                    _onScrollTop.postValue(true)
                }
            }.onFailure {
                _isError.value = true
            }
            _isLoading.value = false
            _refreshing.value = false
            loadMore.set(false)
        }
    }
}
