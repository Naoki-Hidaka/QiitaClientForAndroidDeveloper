package com.example.qiitaclient.domain.model

import java.text.SimpleDateFormat
import java.util.*

data class Article(
    val id: String? = null,
    val title: String? = null,
    val createdAt: String? = null,
    val tags: List<Tag>? = null,
    val url: String? = null
) {
    fun formatDate(): String {
        val stringToDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.JAPANESE)
        val date = stringToDate.parse(createdAt ?: "")
        val dateToString = SimpleDateFormat("yyyy/MM/dd", Locale.JAPANESE)
        return dateToString.format(date ?: Date())
    }



}

data class Tag(
    val name: String? = null
)