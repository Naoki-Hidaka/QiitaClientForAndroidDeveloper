package com.example.qiitaclient.domain.service

import com.example.qiitaclient.domain.model.Article
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IApiClient {

    @GET("tags/Android/items")
    suspend fun getArticles(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Response<List<Article>>

}