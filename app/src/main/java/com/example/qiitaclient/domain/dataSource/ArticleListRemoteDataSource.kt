package com.example.qiitaclient.domain.dataSource

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.domain.service.ApiClient
import kotlinx.coroutines.Dispatchers

object ArticleListRemoteDataSource {

    fun observeArticleList(): LiveData<List<Article>?> {
        return liveData(Dispatchers.IO) {
            runCatching {
                ApiClient.retrofit.getArticles(10, 1)
            }
                .onSuccess {
                    if (it.isSuccessful) {
                        emit(it.body())
                    } else {
                        //TODO errorHandling
                    }
                }
                .onFailure {
                    //TODO errorHandling
                }
        }
    }

    suspend fun getArticleList(callback: suspend (List<Article>?) -> Unit) {
        runCatching {
            ApiClient.retrofit.getArticles(10, 1)
        }
            .onSuccess {
                if (it.isSuccessful) {
                    callback(it.body())
                } else {
                    //TODO errorHandling
                }
            }
            .onFailure {
                //TODO errorHandling
            }
    }
}