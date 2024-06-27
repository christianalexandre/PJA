package com.example.conversaomoedas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.listadecontatos.R
import com.example.listadecontatos.databinding.MainActivityBinding
import kotlinx.parcelize.Parcelize
import java.lang.Double.parseDouble
import java.lang.Integer.parseInt

@SuppressLint("StaticFieldLeak")
private lateinit var binding: MainActivityBinding
private lateinit var currenciesList: Array<String>

@SuppressLint("StaticFieldLeak")
private lateinit var spinnerOne: Spinner

@SuppressLint("StaticFieldLeak")
private lateinit var spinnerTwo: Spinner

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinnerOne = binding.currencyOne
        spinnerTwo = binding.currencyTwo
        binding.calculateButton.setOnClickListener { goToConversionPage() }

        ArrayAdapter.createFromResource(
            this,
            R.array.array_currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerOne.adapter = adapter
            spinnerTwo.adapter = adapter
        }
    }

    private fun goToConversionPage() {

        val spinnerOneSelectedItem = spinnerOne.selectedItem.toString()
        val spinnerTwoSelectedItem = spinnerTwo.selectedItem.toString()
        val initialValue: Double
        currenciesList = arrayOf(spinnerOneSelectedItem, spinnerTwoSelectedItem)

        if (binding.initialValue.text.toString().isBlank()) {
            Toast.makeText(this, "Digite um valor a ser convertido.", Toast.LENGTH_SHORT).show()
            return
        } else {
            initialValue = binding.initialValue.text.toString().toDouble()
        }

        if (spinnerOneSelectedItem == "Selecionar uma moeda" || spinnerTwoSelectedItem == "Selecionar uma moeda") {
            Toast.makeText(this, "Selecione alguma moeda para os dois campos.", Toast.LENGTH_SHORT)
                .show()
        } else if (initialValue <= 0) {
            Toast.makeText(this, "Digite um valor válido.", Toast.LENGTH_SHORT).show()
        } else if (spinnerOneSelectedItem == spinnerTwoSelectedItem) {
            Toast.makeText(this, "Selecione duas moedas diferentes.", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, ConversionPage::class.java).apply {
                val bundle = Bundle().apply {
                    putStringArray("currenciesList", currenciesList)
                    putDouble("initialValue", initialValue)
                }
                putExtra("bundle", bundle)
            }
            startActivity(intent)
        }
    }
}