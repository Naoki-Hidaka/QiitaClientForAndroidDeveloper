package com.example.qiitaclient.domain.repository

import androidx.paging.PagingSource
import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.domain.service.ApiClient
import java.lang.Exception

class ArticlePagingSource : PagingSource<Int, Article>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val result = ApiClient.retrofit.getArticles(perPage = 100, page = params.key ?: 1)

        return try {
            LoadResult.Page(
                data = result.body() ?: listOf(),
                prevKey = null,
                nextKey = params.key
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}