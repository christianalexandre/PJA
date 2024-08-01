package com.example.contactdefinitive

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.contactdefinitive.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var selectedName1: String? = null
    private var selectedName2: String? = null

    private var coinType1: String? = null
    private var coinType2: String? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
        Toast.makeText(this@MainActivity, R.string.first_toast, Toast.LENGTH_LONG).show()
    }

    private fun setupListeners() {
        with(binding) {
            val valueField = editText3.text
            val spinner: Spinner = spin1

            val items = resources.getStringArray(R.array.coins_array1)

            val adapter = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_dropdown_item,
                items
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedName1 = parent.getItemAtPosition(position).toString()
                    coinType1 = selectedName1
                    if (selectedName1 != "Selecionar") {
                        Toast.makeText(
                            this@MainActivity,
                            selectedName1 + " " + getText(R.string.selected_value_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this@MainActivity, R.string.first_toast, Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.anything_selected_toast,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            val items2 = resources.getStringArray(R.array.coins_array2)

            val adapter2 = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_dropdown_item,
                items2
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spin2.adapter = adapter2

            spin2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    selectedName2 = parent.getItemAtPosition(position).toString()
                    coinType2 = selectedName2
                    if (selectedName2 != "Selecionar") {
                        Toast.makeText(
                            this@MainActivity,
                            selectedName2 + " " + getText(R.string.selected_value_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this@MainActivity, R.string.first_toast, Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.anything_selected_toast,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            buttonCalcular.setOnClickListener {
                val text = valueField.toString()
                val valor: Float = if (text != "") text.toFloat() else 0f

                openSecondActivity(valor)
                valueField.clear()
            }

            editText3.addTextChangedListener(TextWatcherHelper.textWatcher(editText3))
        }
    }

    private fun openSecondActivity(valor: Float) {
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

object TextWatcherHelper {
    fun textWatcher(valueField: EditText): TextWatcher {
        return object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //Desenvolve a regra com regex aqui
                val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
                    val pattern = "^\\d*\\.?\\d{0,2}$".toRegex()
                    val result = dest.subSequence(0, dstart).toString() +
                            source.subSequence(start, end) +
                            dest.subSequence(dend, dest.length).toString()
                    if (pattern.matches(result)) {
                        null // Permitir a entrada
                    } else {
                        "" // Rejeitar a entrada
                    }
                }
                valueField.filters = arrayOf(inputFilter)
            }

            override fun afterTextChanged(s: Editable) {
                // No action needed
            }
        }
    }
}