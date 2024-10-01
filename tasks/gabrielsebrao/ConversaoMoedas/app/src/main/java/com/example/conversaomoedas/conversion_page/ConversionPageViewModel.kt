package com.example.conversaomoedas.conversion_page

import androidx.lifecycle.ViewModel

class  ConversionPageViewModel: ViewModel() {

    var convertedValue: Double = 0.0
    var finalValue: Double = 0.0

    // consume api later.
    fun convertValues(initialCurrency: String, finalCurrency: String) {

        when (initialCurrency) {
            "BRL" -> {
                convertedValue *= 1.0
            }

            "USD" -> {
                convertedValue *= 5.3
            }

            "GBP" -> {
                convertedValue *= 6.74
            }

            "CHF" -> {
                convertedValue *= 5.91
            }

            "EUR" -> {
                convertedValue *= 5.72
            }
        }

        when (finalCurrency) {
            "BRL" -> { finalValue = convertedValue }

            "USD" -> { finalValue = convertedValue / 5.3 }

            "GBP" -> { finalValue = convertedValue / 6.74 }

            "CHF" -> { finalValue = convertedValue / 5.91 }

            "EUR" -> { finalValue = convertedValue / 5.72 }
        }

    }

}