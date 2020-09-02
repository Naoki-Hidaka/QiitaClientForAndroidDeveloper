package com.example.qiitaclient.domain.model

import okhttp3.ResponseBody

sealed class ApiResponse<out T> {
    data class Success<out T>(val response: T) : ApiResponse<T>()
    data class ApiError<out T>(val error: ErrorResponse?): ApiResponse<T>()
    data class NetError<out T>(val error: Throwable): ApiResponse<T>()
}