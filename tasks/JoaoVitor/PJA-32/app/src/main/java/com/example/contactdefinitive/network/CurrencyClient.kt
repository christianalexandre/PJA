package com.example.contactdefinitive.network

import com.example.contactdefinitive.ResultModel
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path


interface CurrencyClient {

    @GET("last/{currencies}")
    fun getCurrencyRate(
        @Path("currencies") coinType1 : String
    ): Single<ResultModel>

    @GET("json/available/uniq/")
    fun getCurrencies(): Single<ResponseBody>
}