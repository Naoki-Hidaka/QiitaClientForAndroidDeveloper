package com.example.qiitaclient.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.qiitaclient.domain.model.ApiResponse
import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.domain.repository.ArticleListRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class ArticleListViewModel(application: Application) : AndroidViewModel(application) {

    private val _articleDataList = MutableLiveData(listOf<Article>())
    val articleDataList: LiveData<List<Article>> = _articleDataList

    val isBusy = MutableLiveData(false)

    fun getArticleList() {
        GlobalScope.launch {
            ArticleListRepository.getArticleList().let {
                when (it) {
                    is ApiResponse.Success -> {
                        _articleDataList.postValue(it.response)
                        isBusy.postValue(false)
                    }
                    is ApiResponse.ApiError -> {
                        Timber.e("error${it.error?.message}")
                    }
                    is ApiResponse.NetError -> {

                    }
                }
            }
        }
    }

    fun onRefresh() {
        isBusy.postValue(true)
        getArticleList()
    }
}