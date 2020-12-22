package com.example.qiitaclient.domain.repository

import androidx.lifecycle.LiveData
import com.example.qiitaclient.domain.dataSource.ArticleListLocalDataSource
import com.example.qiitaclient.domain.dataSource.ArticleListRemoteDataSource
import com.example.qiitaclient.domain.model.ArticleWithTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ArticleListRepository(
    private val remoteDataSource: ArticleListRemoteDataSource,
    private val localDataSource: ArticleListLocalDataSource
) {

    fun observeArticleList(): LiveData<List<ArticleWithTag>?> {
        return localDataSource.observeArticleList()
    }

    suspend fun getArticleList(): List<ArticleWithTag> {
        return localDataSource.getArticleList()
    }

    suspend fun refreshArticleList() {
        localDataSource.deleteAllArticle()
        withContext(Dispatchers.IO) {
            remoteDataSource.getArticleList {
                it?.forEach {
                    Timber.d("debug: save$")
                    localDataSource.saveArticle(it)
                }
            }
        }
    }
}

