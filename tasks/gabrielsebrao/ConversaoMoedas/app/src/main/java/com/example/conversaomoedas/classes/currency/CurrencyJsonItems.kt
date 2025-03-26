package com.example.conversaomoedas.classes.currency

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyJsonItems(
    @Json(name = "bid")
    val bid: String,
)
