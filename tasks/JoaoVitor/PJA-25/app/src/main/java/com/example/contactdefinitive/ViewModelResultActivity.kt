package com.example.contactdefinitive

import androidx.lifecycle.ViewModel

class ViewModelResultActivity : ViewModel() {

    fun calculatingResult(coinType1: String?, coinType2: String?, value: Float?): Float {
        var result: Float = 0f

        value?.let {
            val exchangeRates = mapOf(
                "BRL" to "USD" to 0.18f,
                "BRL" to "GBP" to 0.14f,
                "BRL" to "CHF" to 0.16f,
                "BRL" to "EUR" to 0.17f,

                "USD" to "BRL" to 5.42f,
                "USD" to "GBP" to 0.75f,
                "USD" to "CHF" to 0.85f,
                "USD" to "EUR" to 0.90f,

                "GBP" to "BRL" to 7.20f,
                "GBP" to "USD" to 1.33f,
                "GBP" to "CHF" to 1.13f,
                "GBP" to "EUR" to 1.19f,

                "CHF" to "BRL" to 6.38f,
                "CHF" to "USD" to 1.18f,
                "CHF" to "GBP" to 0.89f,
                "CHF" to "EUR" to 1.06f,

                "EUR" to "BRL" to 6.04f,
                "EUR" to "USD" to 1.12f,
                "EUR" to "GBP" to 0.84f,
                "EUR" to "CHF" to 0.95f,
                )

            val rate: Float = exchangeRates[coinType1 to coinType2]!!.toFloat()
            result = rate * value
        }
        return result
    }
}