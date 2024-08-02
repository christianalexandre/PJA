package com.example.contactdefinitive

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.contactdefinitive.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val contatoList: MutableList<Contact> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListener()
    }

    private fun openSecondActivity() {
        val intent = Intent(this, ListContactActivity::class.java).apply {
            val bundle = Bundle().apply {
                putParcelableArrayList("contact", contatoList.toCollection(ArrayList()))

            }
            putExtra("bundle", bundle)
        }
        startActivity(intent)
    }

    private fun clearEditText() {
        with(binding) {
            editNome.text.clear()
            editTelefone.text.clear()
        }
    }

    private fun setupListener() {
        with(binding) {

            buttonSalvarContato.setOnClickListener {
                if (editNome.text.toString() == "" || editTelefone.text.toString() == "") {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.null_fields_toast),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }
                if (contatoList.size >= 3) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.full_fields_toast),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }
                contatoList.add(
                    Contact(
                        editNome.text.toString(),
                        editTelefone.text.toString()
                    )
                )
                clearEditText()
                Toast.makeText(
                    applicationContext,
                    getString(R.string.save_fields_toast),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            editTelefone.addTextChangedListener(TextWatcherHelper.textWatcher(binding.editTelefone))

            buttonLimparContatos.setOnClickListener {
                contatoList.removeAll(contatoList)
                Toast.makeText(
                    applicationContext,
                    getString(R.string.null_list_toast),
                    Toast.LENGTH_SHORT
                ).show()
            }

            buttonListaContatos.setOnClickListener {
                openSecondActivity()
            }
        }

    }

    object TextWatcherHelper {
        fun textWatcher(valueField: EditText): TextWatcher {
            return object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // No action needed
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                    val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
                        val pattern = "^\\d{0,11}\$".toRegex()
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
}