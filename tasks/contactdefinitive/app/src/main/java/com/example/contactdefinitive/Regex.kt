@file:Suppress("NAME_SHADOWING")

package com.example.contactdefinitive

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText

object Regex {
        fun textNumber(valueField: EditText): TextWatcher {
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
    fun textName(valueField: EditText): TextWatcher {
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
                    val pattern = "^[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ0-9 ]{1,30}$".toRegex()
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


