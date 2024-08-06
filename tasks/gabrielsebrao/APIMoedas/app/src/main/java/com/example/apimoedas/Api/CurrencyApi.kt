package com.example.apimoedas.Api

import com.example.apimoedas.Api.Currency
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApi {
    @GET("USD,EUR,CHF,GBP")
    suspend fun getCurrencies(): Response<Currency>
}