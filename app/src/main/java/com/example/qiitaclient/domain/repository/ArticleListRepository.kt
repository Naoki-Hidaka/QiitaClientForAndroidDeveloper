package com.example.qiitaclient.domain.repository

import androidx.lifecycle.LiveData
import com.example.qiitaclient.domain.dataSource.ArticleListLocalDataSource
import com.example.qiitaclient.domain.dataSource.ArticleListRemoteDataSource
import com.example.qiitaclient.domain.model.ArticleWithTag
import dagger.Reusable
import javax.inject.Inject

@Reusable
class ArticleListRepository @Inject constructor(
    private val remoteDataSource: ArticleListRemoteDataSource,
    private val localDataSource: ArticleListLocalDataSource
) {

    fun getArticleList(): LiveData<List<ArticleWithTag>> {
        return localDataSource.getArticleList()
    }

    suspend fun refreshArticleList(page: Int) {
        localDataSource.saveArticles(remoteDataSource.getArticleList(page))
    }
}

