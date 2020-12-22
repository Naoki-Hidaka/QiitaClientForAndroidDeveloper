package com.example.qiitaclient.domain.dataSource

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.domain.model.ArticleWithTag

class ArticleListLocalDataSource(
    private val articleListDao: ArticleListDao
) {

    fun observeArticleList(): LiveData<List<ArticleWithTag>?> {
        return articleListDao.observeArticleList()
    }

    suspend fun getArticleList(): List<ArticleWithTag> {
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
    fun observeArticleList(): LiveData<List<ArticleWithTag>?>

    @Transaction
    @Query("SELECT * FROM article")
    suspend fun getArticleList(): List<ArticleWithTag>

    @Insert
    suspend fun saveArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM article")
    suspend fun deleteAllArticle()
}