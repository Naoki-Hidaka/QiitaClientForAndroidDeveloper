package com.example.qiitaclient.domain.repository

import com.example.qiitaclient.domain.model.ApiResponse
import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.domain.model.ErrorResponse
import com.example.qiitaclient.domain.service.ApiClient
import com.example.qiitaclient.domain.service.JsonHandler

object ArticleListRepository {

    suspend fun getArticleList(): ApiResponse<List<Article>?> {
        ApiClient.retrofit.getArticles(100, 1).let {
            return if (it.isSuccessful) {
                ApiResponse.Success(it.body())
            } else {
                ApiResponse.ApiError(JsonHandler.converter.fromJson(it.errorBody()?.string(), ErrorResponse::class.java))
            }
        }
    }
}
