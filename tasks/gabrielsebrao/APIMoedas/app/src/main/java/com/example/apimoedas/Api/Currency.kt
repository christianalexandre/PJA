package com.example.apimoedas.Api

import com.google.gson.annotations.SerializedName

data class Currency(
    @SerializedName("USDBRL")
    val USDBRL: CurrencyItem,
    @SerializedName("EURBRL")
    val EURBRL: CurrencyItem,
    @SerializedName("CHFBRL")
    val CHFBRL: CurrencyItem,
    @SerializedName("GBPBRL")
    val GBPBRL: CurrencyItem,
)
