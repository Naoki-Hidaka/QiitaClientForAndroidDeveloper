package com.example.qiitaclient.domain.model

import androidx.room.*
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Article(
    @PrimaryKey
    val id: String,
    val title: String,
    val createdAt: String,
    val url: String
) {
    fun formatDate(): String {
        val stringToDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.JAPANESE)
        val date = stringToDate.parse(createdAt)
        val dateToString = SimpleDateFormat("yyyy/MM/dd", Locale.JAPANESE)
        return dateToString.format(date ?: Date())
    }
}

@Entity(
    foreignKeys = [ForeignKey(
        entity = Article::class,
        parentColumns = ["id"],
        childColumns = ["name"]
    )]
)
data class Tag(
    @PrimaryKey
    val name: String
)

data class ArticleWithTag(
    @Embedded
    val article: Article,
    @Relation(
        parentColumn = "id",
        entityColumn = "name"
    )
    val tags: List<Tag>?
)