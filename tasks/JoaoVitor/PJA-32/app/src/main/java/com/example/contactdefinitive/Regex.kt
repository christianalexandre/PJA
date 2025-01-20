package com.example.contactdefinitive

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast

object Regex {
    fun textWatcher(valueField: EditText): TextWatcher {
        return object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //Desenvolve a regra com regex aqui
                val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
                    val pattern = "^\\d{1,5}\\.?\\d{0,2}$".toRegex()
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
