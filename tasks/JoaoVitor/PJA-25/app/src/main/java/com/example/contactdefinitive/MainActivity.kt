package com.example.contactdefinitive

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.contactdefinitive.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var coinType1: String? = null
    private var coinType2: String? = null
    private var firstInit = false
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            val spinner: Spinner = spin1

            val items = resources.getStringArray(R.array.coins_array1)

            val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, items)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    coinType1 = parent.getItemAtPosition(position).toString()

                    if (firstInit) {
                        Toast.makeText(this@MainActivity, coinType1 + " " + getText(R.string.selected_value_toast), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Bem vindo(a)", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(this@MainActivity, R.string.anything_selected_toast, Toast.LENGTH_SHORT).show()
                }
            }

            val items2 = resources.getStringArray(R.array.coins_array2)

            val adapter2 = ArrayAdapter(
                this@MainActivity, android.R.layout.simple_spinner_dropdown_item, items2)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spin2.adapter = adapter2

            spin2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    coinType2 = parent.getItemAtPosition(position).toString()

                    if (firstInit) {
                        Toast.makeText(this@MainActivity, coinType2 + " " + getText(R.string.selected_value_toast), Toast.LENGTH_SHORT).show()
                    } else {
                        firstInit = true
                        Toast.makeText(this@MainActivity, "Bem vindo(a)", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(this@MainActivity, R.string.anything_selected_toast, Toast.LENGTH_SHORT).show()
                }
            }

            buttonCalculating.setOnClickListener {
                val text = editText3.text.toString()
                val valor : Float = if (text != "") text.toFloat() else 0f

                if(valor != 0f && coinType1 != coinType2) {
                    openResultActivity(valor)
                    editText3.text.clear()
                }
                else {
                    Toast.makeText(this@MainActivity, R.string.required_value_toast, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            editText3.addTextChangedListener(Regex.textWatcher(editText3))
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