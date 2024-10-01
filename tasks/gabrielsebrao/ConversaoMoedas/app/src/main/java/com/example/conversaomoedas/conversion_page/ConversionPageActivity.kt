package com.example.conversaomoedas.conversion_page

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.conversaomoedas.classes.Currency
import com.example.conversaomoedas.home_screen.HomeScreenActivity
import com.example.listadecontatos.R
import com.example.listadecontatos.databinding.ActivityConversionPageBinding
import java.text.NumberFormat
import java.util.Locale

class ConversionPageActivity : ComponentActivity() {

    private lateinit var binding: ActivityConversionPageBinding
    private lateinit var conversionPageViewModel: ConversionPageViewModel

    private lateinit var initialCurrency: Currency
    private lateinit var finalCurrency: Currency

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityConversionPageBinding.inflate(layoutInflater)
        conversionPageViewModel = ViewModelProvider(this)[ConversionPageViewModel::class.java]

        initialCurrency = Currency()
        finalCurrency = Currency()

        getExtras()
        finalCurrency.value = conversionPageViewModel.finalValue

        with(conversionPageViewModel) {
            convertedValue = initialCurrency.value
            convertValues(initialCurrency.code, finalCurrency.code)
            finalCurrency.value = finalValue
        }

        setupView()
        setupListeners()

    }

    private fun getExtras() {

        val bundle = intent.getBundleExtra("bundle") ?: return

        initialCurrency.name = bundle.getString("initialCurrency") ?: return
        finalCurrency.name = bundle.getString("finalCurrency") ?: return
        initialCurrency.value = bundle.getDouble("initialValue")
        Log.e("coiso", "bundle: ${bundle.getDouble("initialValue")}")
    }

    private fun setupView() {

        setContentView(binding.root)

        setupCurrencyView(initialCurrency.code, binding.flagOne, binding.initialValue, initialCurrency.value)
        setupCurrencyView(finalCurrency.code, binding.flagTwo, binding.finalValue, conversionPageViewModel.finalValue)

    }

    // consume api later
    private fun setupCurrencyView(currencyCode: String, flag: ImageView, textView: TextView, value: Double) {

        when (currencyCode) {
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

        textView.text = String.format(Locale("pt", "BR"), "%s %s", formatCurrencyValue(value), currencyCode)

    }

    private fun formatCurrencyValue(value: Double): String {

        var formattedValue = String.format(Locale("pt", "BR"), "%.2f", value)

        formattedValue = formattedValue.replace(",", ".")
        formattedValue = NumberFormat.getNumberInstance().format(formattedValue.toDoubleOrNull() ?: 0.0)

        val numberWithNoDecimalPlacesPattern = "^(.[^,]*)$".toRegex()
        val numberWithOneDecimalPlacePattern = "^(.*)(,)(\\d)$".toRegex()

        if(numberWithNoDecimalPlacesPattern.matches(formattedValue)) formattedValue += ",00"
        if(numberWithOneDecimalPlacePattern.matches(formattedValue)) formattedValue += "0"

        return formattedValue

    }

    private fun setupListeners() {

        binding.returnButton.setOnClickListener { goToMainActivity() }

    }

    private fun goToMainActivity() {

        startActivity(Intent(this, HomeScreenActivity::class.java).apply {
            putExtra("bundle", Bundle().apply {
                putString("initialCurrency", initialCurrency.name)
                putString("finalCurrency", finalCurrency.name)
            })
        })

    }

}