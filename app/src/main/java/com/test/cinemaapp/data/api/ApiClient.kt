package com.test.cinemaapp.data.api

import com.test.cinemaapp.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object{
        val retrofit : Retrofit by lazy {
            retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .client(
                    OkHttpClient.Builder()
                        .readTimeout(120, TimeUnit.SECONDS)
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .addInterceptor { chain ->
                            chain.proceed(chain.request())
                        }
                        .build())
                .build()
        }

        fun create(): ApiServiceInterface {
            return retrofit.create(
                ApiServiceInterface::class.java)
        }
    }

}