package com.example.qiitaclient.domain.dispatcher

import com.example.qiitaclient.domain.model.ApiState
import com.example.qiitaclient.domain.model.Article
import com.example.qiitaclient.domain.model.ErrorResponse
import com.example.qiitaclient.domain.service.ApiClient
import okhttp3.ResponseBody

object ArticleListDispatcher {


}

fun ResponseBody.toErrorBody() = ErrorResponse(this.string())