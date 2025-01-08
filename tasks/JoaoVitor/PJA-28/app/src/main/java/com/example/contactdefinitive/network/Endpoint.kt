package com.example.contactdefinitive.network

import com.example.contactdefinitive.ResultModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface Endpoint {

    @GET("last/{currencies}")
    fun getCurrencyRate(
        @Path("currencies") coinType1 : String
    ): Single<ResultModel>

    //    @GET("/gh/fawazahmed0/currency-api@1/latest/currencies.json")
//    fun getCurriences() : Call<JsonObject>
}