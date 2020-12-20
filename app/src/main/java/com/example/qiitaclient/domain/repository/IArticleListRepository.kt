package com.example.qiitaclient.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.domain.model.ErrorResponse
import com.example.qiitaclient.domain.service.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import okhttp3.ResponseBody

object IArticleListRepository : ArticleListRepository {

    override suspend fun getArticleList(
        callback: suspend (List<Article>?) -> Unit,
        errorCallback: suspend (ErrorResponse?) -> Unit,
        fallback: suspend () -> Unit
    ) {
        runCatching {
            ApiClient.retrofit.getArticles(100, 1)
        }
            .onSuccess {
                if (it.isSuccessful) {
                    callback(it.body())
                } else {
                    errorCallback(it.errorBody()?.toErrorBody())
                }
            }
            .onFailure {
                fallback()
            }
    }

    override fun getArticleList(): LiveData<List<Article>?> {
        return liveData(Dispatchers.IO) {
            runCatching {
                ApiClient.retrofit.getArticles(100, 1)
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

    override suspend fun refreshArticleList() {
        delay(1000)
    }
}

interface ArticleListRepository {

    suspend fun getArticleList(
        callback: suspend (List<Article>?) -> Unit,
        errorCallback: suspend (ErrorResponse?) -> Unit,
        fallback: suspend () -> Unit
    )

    fun getArticleList(): LiveData<List<Article>?>

    suspend fun refreshArticleList()
}

fun ResponseBody.toErrorBody() = ErrorResponse(this.string())
