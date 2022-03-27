package com.example.qiitaclient.domain.dataSource

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.domain.model.ArticleWithTag

class ArticleListLocalDataSource(
    private val articleListDao: ArticleListDao
) {

    fun getArticleList(): LiveData<List<ArticleWithTag>> {
        return articleListDao.getArticleList()
    }

    suspend fun saveArticles(articles: List<Article>) {
        articleListDao.saveArticles(articles)
    }
}

@Dao
interface ArticleListDao {
    @Transaction
    @Query("SELECT * FROM article")
    fun getArticleList(): LiveData<List<ArticleWithTag>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveArticles(articles: List<Article>)
}
