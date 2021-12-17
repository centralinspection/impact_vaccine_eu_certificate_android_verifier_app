package com.owner.dgcverifier.network

import com.owner.dgcverifier.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Retrofit {
    //region Http Client
    private val httpClient: OkHttpClient.Builder by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
    }

    private fun buildClient(interceptor: Interceptor? = null, client: OkHttpClient.Builder = httpClient): Retrofit {
        interceptor?.let { client.addInterceptor(it) }

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BuildConfig.HOST_ADDRESS)
            .client(client.build())
            .build()
    }

    var services = buildClient().create(Services::class.java)
}