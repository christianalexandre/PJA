package com.example.conversaomoedas.home_screen

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.conversaomoedas.conversion_page.ConversionPageActivity
import com.example.conversaomoedas.classes.Currency
import com.example.listadecontatos.R
import com.example.listadecontatos.databinding.ActivityHomeScreenBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class HomeScreenActivity : ComponentActivity() {

    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var bundle: Bundle
    private lateinit var homeScreenViewModel: HomeScreenViewModel

    private lateinit var initialCurrency: Currency
    private lateinit var finalCurrency: Currency

    private lateinit var initialCurrencySpinner: Spinner
    private lateinit var finalCurrencySpinner: Spinner
    private lateinit var convertingValueEditText: TextInputEditText
    private lateinit var convertingValueInputLayout: TextInputLayout
    private lateinit var calculateButton: Button
    private lateinit var root: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        homeScreenViewModel = ViewModelProvider(this)[HomeScreenViewModel::class.java]

        initialCurrency = Currency()
        finalCurrency = Currency()

        initialCurrencySpinner = binding.currencyOne
        finalCurrencySpinner = binding.currencyTwo
        convertingValueEditText = binding.initialValueEditText
        convertingValueInputLayout = binding.initialValueInputLayout
        calculateButton = binding.calculateButton
        root = binding.root

        getExtras()

        setupSpinners()
        convertingValueInputLayout.prefixText = initialCurrency.code

        setupListeners()

        convertingValueEditText.setText("") // for some reason textWatcher doesn't work on first change in initialValue, so i put the first change here. now all changes will be watched by textWatcher.
        setContentView(root)

    }

    private fun getExtras() {

        bundle = intent.getBundleExtra("bundle") ?: return
        initialCurrency.name = bundle.getString("initialCurrency") ?: return
        finalCurrency.name = bundle.getString("finalCurrency") ?: return

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

        defineSpinnerSelectedItem(initialCurrencySpinner, initialCurrency.name)
        defineSpinnerSelectedItem(finalCurrencySpinner, finalCurrency.name)

    }

    // consume api later
    private fun defineSpinnerSelectedItem(spinner: Spinner, currency: String) {

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

    private fun setupListeners() {

        setupCalculateButtonListeners()
        setupConvertingValueEditTextListeners()
        setupInitialCurrencySpinnerListeners()
        setupRootListeners()

    }

    private fun setupCalculateButtonListeners() {

        calculateButton.setOnClickListener {
            initialCurrency.name = initialCurrencySpinner.selectedItem.toString()
            finalCurrency.name = finalCurrencySpinner.selectedItem.toString()

            if (!verifyIsAbleToGoToConversionPage()) return@setOnClickListener

            initialCurrency.value = convertingValueEditText.text.toString().replace("(\\.)".toRegex(), "").replace("(,)".toRegex(), ".").toDoubleOrNull() ?: 0.0

            goToConversionPage()
        }

    }

    private fun setupConvertingValueEditTextListeners() {

        convertingValueEditText.setOnKeyListener { _, keyCode, event ->

            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                initialCurrency.name = initialCurrencySpinner.selectedItem.toString()
                finalCurrency.name = finalCurrencySpinner.selectedItem.toString()

                if (!verifyIsAbleToGoToConversionPage()) return@setOnKeyListener false

                initialCurrency.value = convertingValueEditText.text.toString().replace("(\\.)".toRegex(), "").replace("(,)".toRegex(), ".").toDoubleOrNull() ?: 0.0

                goToConversionPage()
            }

            false // for some reason, if returns true it is not possible to digit on edittext anymore.

        }

        convertingValueEditText.setOnClickListener {

            convertingValueEditText.setSelection(convertingValueEditText.text.toString().length)

        }

        with(homeScreenViewModel) {
            convertingValueEditText.addTextChangedListener( filterTextChangedForInitialValue(convertingValueEditText) )
            convertingValueEditText.addTextChangedListener( formatTextChangedForInitialValue(convertingValueEditText) )
            convertingValueEditText.addTextChangedListener( disableErrorModeOnTextChanged(convertingValueInputLayout) )
        }

    }

    private fun setupInitialCurrencySpinnerListeners() {

        initialCurrencySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                initialCurrency.name = initialCurrencySpinner.selectedItem.toString()
                convertingValueInputLayout.prefixText = initialCurrency.code
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

    }

    private fun setupRootListeners() {

        root.viewTreeObserver.addOnGlobalLayoutListener {

            val rect = Rect()
            root.getWindowVisibleDisplayFrame(rect)

            val screenHeight = root.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            val isKeyboardVisible = keypadHeight > (screenHeight * 0.15)

            if(!isKeyboardVisible) {
                convertingValueInputLayout.clearFocus()
                if(convertingValueEditText.length() == 0) convertingValueInputLayout.prefixText = ""
                return@addOnGlobalLayoutListener
            }

            if(!convertingValueEditText.isFocused) convertingValueInputLayout.requestFocus()

            initialCurrency.name = initialCurrencySpinner.selectedItem.toString()
            convertingValueInputLayout.prefixText = initialCurrency.code

        }

    }

    private fun verifyIsAbleToGoToConversionPage(): Boolean {

        if (verifyIsBlank()) return false
        if (verifyAreMissingSelectedCurrencies()) return false
        if (verifyAreIdenticalSelectedCurrencies()) return false
        return true

    }

    private fun verifyIsBlank(): Boolean {

        if (convertingValueEditText.text.toString().isBlank()) {
            convertingValueInputLayout.error = resources.getText(R.string.missing_convert_value)
            return true
        }
        return false

    }

    private fun verifyAreMissingSelectedCurrencies(): Boolean {

        if (initialCurrency.name == "Selecionar uma moeda" || finalCurrency.name == "Selecionar uma moeda") {
            convertingValueInputLayout.error = resources.getText(R.string.missing_selected_currencies)
            return true
        }
        return false

    }

    private fun verifyAreIdenticalSelectedCurrencies(): Boolean {

        if (initialCurrency.name == finalCurrency.name) {
            convertingValueInputLayout.error = resources.getText(R.string.identical_currencies)
            return true
        }
        return false

    }

    private fun goToConversionPage() {

        startActivity(Intent(this, ConversionPageActivity::class.java).apply {
            putExtra("bundle", Bundle().apply {
                putString("initialCurrency", initialCurrency.name)
                putString("finalCurrency", finalCurrency.name)
                putDouble("initialValue", initialCurrency.value)
            })
        })

    }

}