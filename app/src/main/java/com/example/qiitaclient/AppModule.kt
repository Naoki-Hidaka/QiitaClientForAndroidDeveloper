package com.example.qiitaclient

import android.content.Context
import androidx.room.Room
import com.example.qiitaclient.domain.dataSource.ArticleListDao
import com.example.qiitaclient.domain.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideArticleListDao(
        appDatabase: AppDatabase
    ): ArticleListDao {
        return appDatabase.articleListDao()
    }
}
