package com.example.qiitaclient.domain.dataSource

import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.domain.service.ApiClient
import javax.inject.Inject

class ArticleListRemoteDataSource @Inject constructor() {

    suspend fun getArticleList(page: Int): List<Article> {
        return ApiClient.retrofit.getArticles(20, page).body()!!
    }
}
