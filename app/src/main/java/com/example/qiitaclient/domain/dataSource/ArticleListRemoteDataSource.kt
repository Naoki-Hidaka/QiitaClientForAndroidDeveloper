package com.example.qiitaclient.domain.dataSource

import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.domain.service.ApiClient

object ArticleListRemoteDataSource {

    suspend fun getArticleList(): List<Article> {
        return ApiClient.retrofit.getArticles(20, 1).body()!!
    }
}
