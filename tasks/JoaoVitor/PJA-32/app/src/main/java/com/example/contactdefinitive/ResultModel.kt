package com.example.contactdefinitive

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// Get rate
@JsonClass(generateAdapter = true)
data class ResultModel(
    val firstCurrencies: Map<String, askValueFromJson>,
    val secondCurrencies: Map<String, askValueFromJson>
)

@JsonClass(generateAdapter = true)
data class askValueFromJson(
    @Json(name = "ask")
    val coinValue: String = ""
)

// Get currencies


