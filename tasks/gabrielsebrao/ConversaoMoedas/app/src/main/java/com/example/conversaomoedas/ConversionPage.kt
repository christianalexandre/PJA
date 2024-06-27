package com.example.conversaomoedas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.listadecontatos.R
import com.example.listadecontatos.databinding.ConversionPageBinding

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ConversionPageBinding
private lateinit var currenciesList: Array<String>
private var initialValue: Double = 0.0

class ConversionPage : ComponentActivity() {
    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ConversionPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.getBundleExtra("bundle")

        currenciesList = bundle?.getStringArray("currenciesList")!!
        val currencyOne = getCurrencyAbbreviation(currenciesList[0])
        val currencyTwo = getCurrencyAbbreviation(currenciesList[1])
        initialValue = bundle.getDouble("initialValue")
        val finalValue = calculateConversion(currencyOne, currencyTwo, initialValue)

        binding.initialValue.text = String.format("%.2f $currencyOne", initialValue)
        binding.finalValue.text = String.format("%.2f $currencyTwo", finalValue)
        binding.returnButton.setOnClickListener { goToMainActivity() }

    }

    private fun calculateConversion(
        currencyOne: String,
        currencyTwo: String,
        initialValue: Double
    ): Double {
        val brl = 1.0
        val usd = 5.3
        val gbp = 6.74
        val chf = 5.91
        val eur = 5.72

        var valueOne = initialValue
        var valueTwo = 0.0

        when (currencyOne) {
            "BRL" -> {
                valueOne *= brl
                binding.flagOne.setImageResource(R.drawable.brflag)
                binding.flagOne.contentDescription = "Ícone da Bandeira do Brasil"
            }

            "USD" -> {
                valueOne *= usd
                binding.flagOne.setImageResource(R.drawable.usflag)
                binding.flagOne.contentDescription = "Ícone da bandeira dos Estados Unidos"
            }

            "GBP" -> {
                valueOne *= gbp
                binding.flagOne.setImageResource(R.drawable.ukflag)
                binding.flagOne.contentDescription = "Ícone da bandeira do Reino Unido"
            }

            "CHF" -> {
                valueOne *= chf
                binding.flagOne.setImageResource(R.drawable.chflag)
                binding.flagOne.contentDescription = "Ícone da bandeira da Suíça"
            }

            "EUR" -> {
                valueOne *= eur
                binding.flagOne.setImageResource(R.drawable.euflag)
                binding.flagOne.contentDescription = "Ícone da bandeira da União Europeia"
            }
        }
        when (currencyTwo) {
            "BRL" -> {
                valueTwo = brl
                binding.flagTwo.setImageResource(R.drawable.brflag)
                binding.flagTwo.contentDescription = "Ícone da Bandeira do Brasil"
            }

            "USD" -> {
                valueTwo = usd
                binding.flagTwo.setImageResource(R.drawable.usflag)
                binding.flagTwo.contentDescription = "Ícone da bandeira dos Estados Unidos"
            }

            "GBP" -> {
                valueTwo = gbp
                binding.flagTwo.setImageResource(R.drawable.ukflag)
                binding.flagTwo.contentDescription = "Ícone da bandeira do Reino Unido"
            }

            "CHF" -> {
                valueTwo = chf
                binding.flagTwo.setImageResource(R.drawable.chflag)
                binding.flagTwo.contentDescription = "Ícone da bandeira da Suíça"
            }

            "EUR" -> {
                valueTwo = eur
                binding.flagTwo.setImageResource(R.drawable.euflag)
                binding.flagTwo.contentDescription = "Ícone da bandeira da União Europeia"
            }
        }

        return valueOne / valueTwo
    }

    private fun getCurrencyAbbreviation(currency: String): String {
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

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            val bundle = Bundle().apply {
                putStringArray("currenciesList", currenciesList)
                putDouble("initialValue", initialValue)
            }
            putExtra("bundle", bundle)
        }
        startActivity(intent)
    }
}