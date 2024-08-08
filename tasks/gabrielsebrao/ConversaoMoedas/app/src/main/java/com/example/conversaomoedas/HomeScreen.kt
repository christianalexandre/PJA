package com.example.conversaomoedas

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.conversaomoedas.objects.Currency
import com.example.conversaomoedas.objects.TextWatcherHelper
import com.example.listadecontatos.R
import com.example.listadecontatos.databinding.ActivityHomeScreenBinding
import kotlin.properties.Delegates


class HomeScreen : ComponentActivity() {
    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var bundle: Bundle
    private lateinit var initialCurrencySpinner: Spinner
    private lateinit var finalCurrencySpinner: Spinner
    private lateinit var initialCurrencySpinnerSelectedItem: String
    private lateinit var finalCurrencySpinnerSelectedItem: String
    private lateinit var initialValueComponent: EditText
    private var convertingValue by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        initialCurrencySpinner = binding.currencyOne
        finalCurrencySpinner = binding.currencyTwo
        initialCurrencySpinnerSelectedItem = "Selecionar uma moeda"
        finalCurrencySpinnerSelectedItem = "Selecionar uma moeda"
        initialValueComponent = binding.initialValue

        getExtras()
        setupSpinners()
        setupListeners()

        initialValueComponent.setText("") // for some reason textWatcher doesn't work on first change in initialValue, so i put the first change here. now all changes will fall into textWatcher
        setContentView(binding.root)
    }

    private fun getExtras() {
        bundle = intent.getBundleExtra("bundle") ?: return
        initialCurrencySpinnerSelectedItem = bundle.getStringArray("currenciesList")!![0]
        finalCurrencySpinnerSelectedItem = bundle.getStringArray("currenciesList")!![1]
    }

    private fun setupSpinners() {
        ArrayAdapter.createFromResource(
            this,
            R.array.array_currencies,
            R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
            initialCurrencySpinner.adapter = adapter
            finalCurrencySpinner.adapter = adapter
        }

        Currency.defineCurrencySelectedItem(
            initialCurrencySpinner,
            initialCurrencySpinnerSelectedItem
        )
        Currency.defineCurrencySelectedItem(finalCurrencySpinner, finalCurrencySpinnerSelectedItem)
    }

    private fun setupListeners() {
        binding.calculateButton.setOnClickListener {
            initialCurrencySpinnerSelectedItem = initialCurrencySpinner.selectedItem.toString()
            finalCurrencySpinnerSelectedItem = finalCurrencySpinner.selectedItem.toString()

            if (!verifyIsBlank()) return@setOnClickListener
            if (!verifyIsMissingSelectedCurrencies()) return@setOnClickListener
            if (!verifyIsIdenticalSelectedCurrencies()) return@setOnClickListener

            convertingValue = initialValueComponent.text.toString().replace(',', '.').toDouble()

            goToConversionPage()
        }
        initialValueComponent.addTextChangedListener(TextWatcherHelper.filterChangeTextForInitialValue(initialValueComponent))

        initialValueComponent.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    initialCurrencySpinnerSelectedItem = initialCurrencySpinner.selectedItem.toString()
                    finalCurrencySpinnerSelectedItem = finalCurrencySpinner.selectedItem.toString()

                    if (!verifyIsBlank()) return false
                    if (!verifyIsMissingSelectedCurrencies()) return false
                    if (!verifyIsIdenticalSelectedCurrencies()) return false

                    convertingValue = initialValueComponent.text.toString().replace(',', '.').toDouble()

                    goToConversionPage()
                    return true
                }
                return false
            }
        })
    }
    private fun verifyIsBlank(): Boolean {
        if (initialValueComponent.text.toString().isBlank()) {
            Toast.makeText(this, R.string.missing_convert_value, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun verifyIsMissingSelectedCurrencies(): Boolean {
        if (initialCurrencySpinnerSelectedItem == "Selecionar uma moeda" || finalCurrencySpinnerSelectedItem == "Selecionar uma moeda") {
            Toast.makeText(this, R.string.missing_selected_currencies, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun verifyIsIdenticalSelectedCurrencies(): Boolean {
        if (initialCurrencySpinnerSelectedItem == finalCurrencySpinnerSelectedItem) {
            Toast.makeText(this, R.string.identical_currencies, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun goToConversionPage() {
        startActivity(Intent(this, ConversionPage::class.java).apply {
            putExtra("bundle", Bundle().apply {
                putStringArray(
                    "currenciesList",
                    arrayOf(initialCurrencySpinnerSelectedItem, finalCurrencySpinnerSelectedItem)
                )
                putDouble("initialValue", convertingValue)
            })
        })
    }
}