package com.example.conversaomoedas.classes

class Currency {

    var name: String = "Selecionar uma moeda"

        // consume api later.
        set(name) {

            field = name
            when (field) {
                "Real (BRL)" -> {
                    this.code =  "BRL"
                }

                "Dólar Americano (USD)" -> {
                    this.code = "USD"
                }

                "Libra Esterlina (GBP)" -> {
                    this.code = "GBP"
                }

                "Franco Suíço (CHF)" -> {
                    this.code = "CHF"
                }

                "Euro (EUR)" -> {
                    this.code = "EUR"
                }

                "Selecionar uma moeda" -> {
                    this.code = "$"
                }
            }

        }

    var code: String = "$"
    var value: Double = 0.0

}