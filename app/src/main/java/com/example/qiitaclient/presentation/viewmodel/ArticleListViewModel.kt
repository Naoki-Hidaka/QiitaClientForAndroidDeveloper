package com.example.qiitaclient.presentation.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.*
import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.domain.repository.ArticleListRepository
import com.example.qiitaclient.presentation.viewmodel.common.showErrorDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleListViewModel(
    application: Application,
    private val activity: Activity,
    private val articleRepository: ArticleListRepository
) : AndroidViewModel(application) {

    private val _articleDataList = MutableLiveData(listOf<Article>())
    val articleDataList: LiveData<List<Article>> = _articleDataList

    val isBusy = MutableLiveData(false)

    private fun getArticleList() {
        viewModelScope.launch(Dispatchers.Default) {
            articleRepository.getArticleList(
                {
                    _articleDataList.postValue(it)
                }
            ) {
                withContext(Dispatchers.Main) {
                    showErrorDialog(activity, ::getArticleList)
                }
            }
        }
    }

    init {
        getArticleList()
    }

    fun onRefresh() {
        isBusy.postValue(true)
        getArticleList()
    }

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