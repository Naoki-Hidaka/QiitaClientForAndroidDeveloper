package com.example.qiitaclient.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.qiitaclient.domain.model.ArticleWithTag
import com.example.qiitaclient.domain.repository.ArticleListRepository
import kotlinx.coroutines.launch

class ArticleListViewModel(
    application: Application,
    private val articleRepository: ArticleListRepository
) : AndroidViewModel(application) {

    private val _forcedUpdate = MutableLiveData(false)

    private val _isLoading = MutableLiveData(false)
    val isLoading = _isLoading

    private val _articleList: LiveData<List<ArticleWithTag>?> = _forcedUpdate.switchMap {
        if (it) {
            _isLoading.value = true
            viewModelScope.launch {
                articleRepository.refreshArticleList()
                _isLoading.value = false
            }
        }
        articleRepository.getArticleList().distinctUntilChanged()
    }

    val articleList = _articleList

    init {
        _forcedUpdate.value = true
    }

    fun onRefresh() {
        _forcedUpdate.postValue(true)
    }

    companion object {
        class Factory(
            private val application: Application?,
            private val articleRepository: ArticleListRepository
        ) : ViewModelProvider.AndroidViewModelFactory(application!!) {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>) = ArticleListViewModel(
                application!!,
                articleRepository,
            ) as T
        }
    }
}