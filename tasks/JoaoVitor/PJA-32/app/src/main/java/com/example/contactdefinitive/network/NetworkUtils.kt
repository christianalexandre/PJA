package com.example.contactdefinitive.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


object NetworkUtils {

    val moshi: Moshi
        get() = Moshi
            .Builder()
            .add(ResultModelAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

    fun OkHttpClient.Builder.buildRetrofit(path: String): Retrofit {
        val client = this.build()

        return Retrofit.Builder()
            .baseUrl(path)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }

    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://economia.awesomeapi.com.br/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
}