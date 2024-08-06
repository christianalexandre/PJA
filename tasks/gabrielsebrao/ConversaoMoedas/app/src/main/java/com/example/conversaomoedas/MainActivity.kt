package com.example.conversaomoedas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.conversaomoedas.objects.Currency
import com.example.conversaomoedas.objects.TextWatcherHelper
import com.example.listadecontatos.R
import com.example.listadecontatos.databinding.MainActivityBinding

class MainActivity : ComponentActivity() {
    private lateinit var binding: MainActivityBinding
    private lateinit var bundle: Bundle
    private lateinit var currenciesList: Array<String>
    private lateinit var spinnerOne: Spinner
    private lateinit var spinnerTwo: Spinner
    private lateinit var initialValue: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActivityVariables()
        setupView()
        setupListeners()

        initialValue.setText("") // for some reason textWatcher doesn't work on first change in initialValue, so i put the first change here. now all changes will fall into textWatcher

        getExtras()
    }

    private fun initActivityVariables() {
        binding = MainActivityBinding.inflate(layoutInflater)
        spinnerOne = binding.currencyOne
        spinnerTwo = binding.currencyTwo
        initialValue = binding.initialValue
        setupSpinners()
    }

    private fun setupView() {
        setContentView(binding.root)
    }

    private fun getExtras() {
        bundle = intent.getBundleExtra("bundle") ?: return // if bundle == null returns the function

        val spinnerOneSelectedItem = bundle.getStringArray("currenciesList")!![0]
        val spinnerTwoSelectedItem = bundle.getStringArray("currenciesList")!![1]

        Currency.defineCurrencySelectedItem(spinnerOne, spinnerOneSelectedItem)
        Currency.defineCurrencySelectedItem(spinnerTwo, spinnerTwoSelectedItem)
    }

    private fun setupListeners() {
        binding.calculateButton.setOnClickListener { goToConversionPage() }
        initialValue.addTextChangedListener(TextWatcherHelper.filterChangedText(initialValue))
    }

    private fun goToConversionPage() {
        val spinnerOneSelectedItem = spinnerOne.selectedItem.toString()
        val spinnerTwoSelectedItem = spinnerTwo.selectedItem.toString()
        val convertingValue: Double
        currenciesList = arrayOf(spinnerOneSelectedItem, spinnerTwoSelectedItem)

        if (this.initialValue.text.toString().isBlank()) {
            Toast.makeText(this, R.string.missing_convert_value, Toast.LENGTH_SHORT).show()
            return
        }

        try {
            convertingValue = this.initialValue.text.toString().replace(',','.').toDouble()
        } catch (_: NumberFormatException) {
            Toast.makeText(this, R.string.valid_value, Toast.LENGTH_SHORT).show()
            return
        }

        if (spinnerOneSelectedItem == "Selecionar uma moeda" || spinnerTwoSelectedItem == "Selecionar uma moeda") {
            Toast.makeText(this, R.string.missing_selected_currencies, Toast.LENGTH_SHORT).show()
        } else if (convertingValue <= 0) {
            Toast.makeText(this, R.string.valid_value, Toast.LENGTH_SHORT).show()
        } else if (spinnerOneSelectedItem == spinnerTwoSelectedItem) {
            Toast.makeText(this, R.string.identical_currencies, Toast.LENGTH_SHORT).show()
        } else {
            setupExtras(convertingValue)
        }
    }

    private fun setupExtras(convertingValue: Double) {
        startActivity(Intent(this, ConversionPage::class.java).apply {
            val bundle = Bundle().apply {
                putStringArray("currenciesList", currenciesList)
                putDouble("initialValue", convertingValue)
            }
            putExtra("bundle", bundle)
        })
    }

    @SuppressLint("ResourceType")
    private fun setupSpinners() {
        ArrayAdapter.createFromResource(
            this,
            R.array.array_currencies,
            R.drawable.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
            spinnerOne.adapter = adapter
            spinnerTwo.adapter = adapter
        }
    }
}