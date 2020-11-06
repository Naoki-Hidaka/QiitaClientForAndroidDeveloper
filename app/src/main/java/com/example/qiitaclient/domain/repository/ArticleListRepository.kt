package com.example.qiitaclient.domain.repository


import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.domain.service.ApiClient

object ArticleListRepository {

    suspend fun getArticleList(
        callback: suspend (List<Article>?) -> Unit,
        fallback: suspend () -> Unit
    ) {
        runCatching {
            ApiClient.retrofit.getArticles(100, 1)
        }
            .onSuccess {
                if(it.isSuccessful) {
                    callback(it.body())
                } else {
                    fallback()
                }
            }
            .onFailure {
                fallback()
            }
    }
}
