package com.example.conversaomoedas.objects

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText

object TextWatcherHelper {
    fun filterChangeTextForInitialValue(text: EditText): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
                    val pattern = "^(\\d{0,9})([.,]?)$".toRegex()
                    val decimalPattern = "^(\\d{0,9})([.,]?)(\\d{0,2})$".toRegex()
                    val noNumberPattern = "^([.,])$".toRegex()
                    val zeroPattern = "^(0)([^.,]*)".toRegex()
                    val result = dest.subSequence(0, dstart).toString() +
                            source.subSequence(start, end) +
                            dest.subSequence(dend, dest.length).toString()

                    if (noNumberPattern.matches(result)) {
                        return@InputFilter ""
                    }

                    if (zeroPattern.matches(result) && result != "0") {
                        return@InputFilter ""
                    }

                    if (('.' in result || ',' in result) && decimalPattern.matches(result)) {
                        null
                    } else if (pattern.matches(result)) {
                        null
                    } else {
                        ""
                    }

                }

                text.filters = arrayOf(inputFilter)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
    }
}
