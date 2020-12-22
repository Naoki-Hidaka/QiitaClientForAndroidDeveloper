package com.example.qiitaclient.domain.dataSource

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.domain.model.ArticleWithTag

class ArticleListLocalDataSource(
    private val articleListDao: ArticleListDao
) {

    fun getArticleList(): LiveData<List<ArticleWithTag>?> {
        return articleListDao.getArticleList()
    }

    suspend fun saveArticle(article: Article) {
        articleListDao.saveArticle(article)
    }

    suspend fun deleteArticle(article: Article) {
        articleListDao.deleteArticle(article)
    }

    suspend fun deleteAllArticle() {
        articleListDao.deleteAllArticle()
    }
}

@Dao
interface ArticleListDao {
    @Transaction
    @Query("SELECT * FROM article")
    fun getArticleList(): LiveData<List<ArticleWithTag>?>

    @Insert
    suspend fun saveArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM article")
    suspend fun deleteAllArticle()
}