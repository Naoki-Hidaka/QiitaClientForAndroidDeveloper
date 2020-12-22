package com.example.qiitaclient.domain.model

sealed class ApiState<out T>(val content: StateContent<T?>) {
    data class Success<out T>(val data: StateContent<T?>) : ApiState<T>(data)
    class Loading<out T>(data: StateContent<T?>) : ApiState<T>(data)
    data class ApiError<out T>(val data: StateContent<T?>, val error: ErrorResponse?): ApiState<T>(data)
    data class NetError<out T>(val data: StateContent<T?>, val error: Throwable): ApiState<T>(data)
}

sealed class StateContent<out T> {
    data class Exist<out T>(val rawContent: T?) : StateContent<T>()
    class NotExist<out T> : StateContent<T>()
}