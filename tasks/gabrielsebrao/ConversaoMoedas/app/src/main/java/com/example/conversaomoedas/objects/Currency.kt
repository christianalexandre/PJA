package com.example.conversaomoedas.objects

import android.widget.Spinner

object Currency {
    fun getCurrencyAbbreviation(currency: String): String {
        when (currency) {
            "Real (BRL)" -> {
                return "BRL"
            }

            "Dólar Americano (USD)" -> {
                return "USD"
            }

            "Libra Esterlina (GBP)" -> {
                return "GBP"
            }

            "Franco Suíço (CHF)" -> {
                return "CHF"
            }

            "Euro (EUR)" -> {
                return "EUR"
            }
        }

        return ""
    }

    fun defineCurrencySelectedItem(spinner: Spinner, currency: String) {
        when (currency) {
            "Dólar Americano (USD)" -> {
                spinner.setSelection(1)
            }

            "Libra Esterlina (GBP)" -> {
                spinner.setSelection(2)
            }

            "Franco Suíço (CHF)" -> {
                spinner.setSelection(3)
            }

            "Real (BRL)" -> {
                spinner.setSelection(4)
            }

            "Euro (EUR)" -> {
                spinner.setSelection(5)
            }
        }
    }
}