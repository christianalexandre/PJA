package com.example.conversaomoedas

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.conversaomoedas.objects.Currency
import com.example.listadecontatos.R
import com.example.listadecontatos.databinding.ActivityConversionPageBinding
import kotlin.properties.Delegates

class ConversionPage : ComponentActivity() {

    private lateinit var binding: ActivityConversionPageBinding
    private lateinit var initialCurrency: String
    private lateinit var finalCurrency: String
    private lateinit var currenciesList: Array<String>
    private var initialValue by Delegates.notNull<Double>()
    private var finalValue by Delegates.notNull<Double>()
    private var convertedValue by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConversionPageBinding.inflate(layoutInflater)

        getExtras()
        convertedValue = initialValue // to remember the initial value

        convertValues()
        setupView()
        setupListeners()
    }

    private fun getExtras() {
        val bundle = intent.getBundleExtra("bundle") ?: return

        currenciesList = bundle.getStringArray("currenciesList")!!
        initialCurrency = Currency.getCurrencyAbbreviation(currenciesList[0])
        finalCurrency = Currency.getCurrencyAbbreviation(currenciesList[1])

        initialValue = bundle.getDouble("initialValue")
    }

    private fun convertValues() {
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

    private fun setupView() {
        setContentView(binding.root)

        setupCurrencyView(initialCurrency, binding.flagOne, binding.initialValue, initialValue)
        setupCurrencyView(finalCurrency, binding.flagTwo, binding.finalValue, finalValue)
    }

    private fun setupCurrencyView(currency: String, flag: ImageView, textView: TextView, value: Double) {
        when (currency) {
            "BRL" -> {
                flag.setImageResource(R.drawable.flag_br)
                flag.contentDescription = "Ícone da Bandeira do Brasil"
            }

            "USD" -> {
                flag.setImageResource(R.drawable.flag_us)
                flag.contentDescription = "Ícone da bandeira dos Estados Unidos"
            }

            "GBP" -> {
                flag.setImageResource(R.drawable.flag_uk)
                flag.contentDescription = "Ícone da bandeira do Reino Unido"
            }

            "CHF" -> {
                flag.setImageResource(R.drawable.flag_ch)
                flag.contentDescription = "Ícone da bandeira da Suíça"
            }

            "EUR" -> {
                flag.setImageResource(R.drawable.flag_eu)
                flag.contentDescription = "Ícone da bandeira da União Europeia"
            }
        }

        textView.text = String.format("%.2f $currency", value)
    }

    private fun setupListeners() {
        binding.returnButton.setOnClickListener { goToMainActivity() }
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, HomeScreen::class.java).apply {
            putExtra("bundle", Bundle().apply {
                putStringArray("currenciesList", currenciesList)
            })
        })
    }
}