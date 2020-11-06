package com.example.qiitaclient.presentation.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.domain.repository.ArticleListRepository
import com.example.qiitaclient.domain.repository.ArticlePagingSource
import com.example.qiitaclient.presentation.viewmodel.common.showErrorDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleListViewModel(
    application: Application,
    private val activity: Activity,
    private val articleRepository: ArticleListRepository
) : AndroidViewModel(application) {

    val isBusy = MutableLiveData(false)

    val flow = Pager(
            PagingConfig(pageSize = 20, initialLoadSize = 20)
        ) {
            ArticlePagingSource()
        }.flow

    companion object {
        class Factory(
            private val application: Application?,
            private val activity: Activity?,
            private val articleRepository: ArticleListRepository = ArticleListRepository,
        ) : ViewModelProvider.AndroidViewModelFactory(application!!) {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>) = ArticleListViewModel(
                application!!,
                activity!!,
                articleRepository,
            ) as T
        }
    }
}