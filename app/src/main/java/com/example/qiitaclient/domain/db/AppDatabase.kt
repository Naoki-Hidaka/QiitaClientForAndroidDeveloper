package com.example.qiitaclient.domain.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.qiitaclient.domain.dataSource.ArticleListDao
import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.domain.model.Tag

@Database(entities = [Article::class, Tag::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleListDao(): ArticleListDao
}
