package com.example.qiitaclient.domain.service

import com.example.qiitaclient.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val endPoint = BuildConfig.END_POINT
    private const val accessToken = BuildConfig.ACCESS_TOKEN

    private val logging = HttpLoggingInterceptor().also {
        it.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            chain
                .request()
                .newBuilder()
                .header("Authorization", "Bearer 82a4ddf067209d74c06afff8a6aa9d1de0b14c62")
                .build()
                .let {
                    chain.proceed(it)
                }
        })
        .addInterceptor(logging)
        .build()

    val retrofit: IApiClient = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(JsonHandler.converter))
        .baseUrl(endPoint)
        .client(okHttpClient)
        .build()
        .create(IApiClient::class.java)
}
