package com.example.pja_28.util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkUtils {
    companion object{
        fun getRetrofitInstance(path: String) : Retrofit{
            return Retrofit.Builder()
            .baseUrl(path)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        }
    }
}