package com.example.contactdefinitive

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactdefinitive.databinding.ActivityMainBinding
import com.example.contactdefinitive.network.MainViewModel

class MainActivity : AppCompatActivity() {

    private var coinType1: String? = null
    private var coinType2: String? = null

    private var firstInit = false

    private lateinit var viewModel: MainViewModel

    private var availableCurrenciesMap: Map<String, String> = mapOf()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.setAvailableCurrenciesMap()

        setupListeners()

        setupCurrencyButtonsListeners()

        getCurrenciesFromViewModel()

        getCodeCurrency()
    }

    private fun setupListeners() {
        with(binding) {
            buttonCalculating.setOnClickListener {
                val text = editText3.text.toString()
                val valor: Float = if (text != "") text.toFloat() else 0f

                if (valor != 0f && coinType1 != coinType2) {
                    openResultActivity(valor)
                    editText3.text.clear()
                } else if (coinType1 == coinType2) {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.required_distint_currecy_toast,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else if (coinType1 == "Selecione uma moeda" || coinType2 == "Selecione uma moeda") {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.selected_currency_correct_toast,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.required_value_toast,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }
            editText3.addTextChangedListener(Regex.textWatcher(editText3))

            if (firstInit) {
                print("firstInit")
            } else {
                Toast.makeText(this@MainActivity, "Bem vindo(a)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCurrencyButtonsListeners() {
        val dialogView = layoutInflater.inflate(R.layout.item_dialog, null)

        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerViewDialog)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        binding.spin1.setOnClickListener {
            recyclerView.adapter =
                MapAdapter(availableCurrenciesMap, dialog, viewModel.selectedFirstCurrency)
            dialog.show()
        }

        binding.spin2.setOnClickListener {
            recyclerView.adapter =
                MapAdapter(availableCurrenciesMap, dialog, viewModel.selectedSecCurrency)
            dialog.show()
        }
    }

    private fun getCurrenciesFromViewModel() {
        viewModel.reqSuccess.observe(this) { reqSuccess ->
            if (reqSuccess)
                availableCurrenciesMap = viewModel.availableCurrenciesMap
        }
    }

    private fun getCodeCurrency() {
        with(binding) {
            viewModel.selectedFirstCurrency.observe(this@MainActivity) { selectedCurrency ->
                spin1.text = selectedCurrency
                coinType1 = selectedCurrency.toString().replace(".*\\(".toRegex(), "")
                    .replace("\\)".toRegex(), "")
            }

            viewModel.selectedFirstCurrency.observe(this@MainActivity) { currency ->
                Toast.makeText(
                    this@MainActivity,
                    currency + " " + getText(R.string.selected_value_toast),
                    Toast.LENGTH_SHORT
                ).show()
            }

            viewModel.selectedSecCurrency.observe(this@MainActivity) { selectedCurrency ->
                spin2.text = selectedCurrency
                coinType2 = selectedCurrency.toString().replace(".*\\(".toRegex(), "")
                    .replace("\\)".toRegex(), "")
            }

            viewModel.selectedSecCurrency.observe(this@MainActivity) { currency ->
                Toast.makeText(
                    this@MainActivity,
                    currency + " " + getText(R.string.selected_value_toast),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openResultActivity(valor: Float) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            val bundle = Bundle().apply {
                putFloat("valor", valor)
                putString("coinType1", coinType1)
                putString("coinType2", coinType2)
            }
            putExtra("bundle", bundle)
        }
        startActivity(intent)
    }
}
