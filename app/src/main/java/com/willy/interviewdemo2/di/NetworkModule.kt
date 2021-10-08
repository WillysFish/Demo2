package com.willy.interviewdemo2.di

import com.willy.interviewdemo2.BuildConfig
import com.willy.interviewdemo2.data.api.repo.ApiRepository
import com.willy.interviewdemo2.data.api.service.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

private const val TIME_OUT = 30L

val NetworkModule = module {

    factory { HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY } }

    single { createOkHttpClient(get()) }

    single { createRetrofit(get(), BuildConfig.BASE_URL) }

    single { createApiService(get()) }
}

fun createOkHttpClient(loggingInterceptor: HttpLoggingInterceptor) =
    OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .build()

fun createRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit =
    Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

fun createApiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)

