package com.example.qiitaclient.domain.service

import android.app.Application
import androidx.room.Room
import com.example.qiitaclient.domain.dataSource.ArticleListLocalDataSource
import com.example.qiitaclient.domain.dataSource.ArticleListRemoteDataSource
import com.example.qiitaclient.domain.db.AppDatabase
import com.example.qiitaclient.domain.repository.ArticleListRepository
import timber.log.Timber

class MyApplication : Application() {

    private val db: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database-name"
        ).build()
    }

    override fun onCreate() {
        super.onCreate()

        println()

        Timber.plant(Timber.DebugTree())
    }

    fun provideArticleListRepository(): ArticleListRepository {
        return ArticleListRepository(
            ArticleListRemoteDataSource,
            ArticleListLocalDataSource(db.articleListDao())
        )
    }

}
