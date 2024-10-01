package com.example.conversaomoedas.home_screen

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat

class HomeScreenViewModel: ViewModel() {

    private var isDecimalNumberWithCommaWithoutDecimalPlaces: Boolean = false
    private var previousResult: String = ""

    fun filterTextChangedForInitialValue(editText: EditText): TextWatcher {

        return object : TextWatcher {

            private var onlyOneCommaPattern = "^(,)$".toRegex()
            private var decimalNumberWithDoubleCommasPattern = "^([(\\d*).?]*)(,)(.*)(,)$".toRegex()
            private var decimalNumberThatExceedsTwoDecimalPlacesPattern = "^([(\\d*).?]*)(,)(\\d{2}.)$".toRegex()
            private val decimalNumberThatExceedsFourteenDigitsBeforeCommaPattern = "^([\\d.]{14})$".toRegex()

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(textChanged: CharSequence?, start: Int, before: Int, count: Int) {

                editText.filters = arrayOf(InputFilter { source, begin, end, dest, dstart, dend ->
                    val result = dest.subSequence(0, dstart).toString() + source.subSequence(begin, end) + dest.subSequence(dend, dest.length).toString()

                    if (onlyOneCommaPattern.matches(result)) return@InputFilter ""
                    if (decimalNumberWithDoubleCommasPattern.matches(result)) return@InputFilter ""
                    if (decimalNumberThatExceedsFourteenDigitsBeforeCommaPattern.matches(result)) return@InputFilter ""
                    if (decimalNumberThatExceedsTwoDecimalPlacesPattern.matches(result)) return@InputFilter ""

                    null
                })

                editText.setSelection(editText.text.toString().length)

            }

            override fun afterTextChanged(textChanged: Editable?) {}

        }

    }

    fun formatTextChangedForInitialValue(editText: EditText): TextWatcher {

        return object : TextWatcher {

            private var current = ""

            private var decimalNumberWithCommaAndNoDecimalPlacesPattern = "^([(\\d*).?]*)(,)$".toRegex()
            private var decimalNumberWithOneZeroAfterCommaPattern = "^(.*)(,0)$".toRegex()
            private var decimalNumberWithOneZeroAfterADecimalPlacePattern = "^(.*)(,\\d0)$".toRegex()

            private val decimalNumberWithOneDecimalPlacePattern = "^([(\\d*).?]*)(,)(\\d)$".toRegex()
            private val integerNumberPattern = "^[\\d.?]*$".toRegex()

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(textChanged: CharSequence?, start: Int, before: Int, count: Int) {

                textChanged ?: return
                if (textChanged.toString() != current) {

                    if (textChanged.toString().isEmpty()) return

                    // do not allow the user to change the input if the last character was a comma, UNLESS the next character entered is a decimal place or the comma is being deleted.
                    if(isDecimalNumberWithCommaWithoutDecimalPlaces && (!decimalNumberWithOneDecimalPlacePattern.matches(textChanged) && !integerNumberPattern.matches(textChanged))) {
                        editText.removeTextChangedListener(this)
                        editText.setText(previousResult)
                        editText.addTextChangedListener(this)
                    }

                    // detects if the user entered a comma, stores the current input and returns before formatting with "NumberFormat.getNumberInstance().format(parsed)"
                    if (decimalNumberWithCommaAndNoDecimalPlacesPattern.matches(textChanged.toString())) {
                        isDecimalNumberWithCommaWithoutDecimalPlaces = true
                        previousResult = textChanged.toString()
                        return
                    }

                    isDecimalNumberWithCommaWithoutDecimalPlaces = false

                    if (decimalNumberWithOneZeroAfterCommaPattern.matches(textChanged.toString())) return
                    if (decimalNumberWithOneZeroAfterADecimalPlacePattern.matches(textChanged.toString())) return

                    // i used all the returns above because NumberFormat.getNumberInstance().format(parsed) would remove essential characters under these conditions.

                    val cleanString = textChanged.toString().replace("(\\.)".toRegex(), "").replace("(,)".toRegex(), ".")
                    val parsed = cleanString.toDoubleOrNull() ?: 0.0
                    val formatted = NumberFormat.getNumberInstance().format(parsed)

                    editText.removeTextChangedListener(this)
                    current = formatted
                    editText.setText(formatted)
                    editText.setSelection(formatted.length)
                    editText.addTextChangedListener(this)

                    previousResult = formatted.toString()
                }
            }

            override fun afterTextChanged(textChanged: Editable?) {}
        }

    }

    fun disableErrorModeOnTextChanged(textInputLayout: TextInputLayout): TextWatcher {

        return object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(textChanged: CharSequence?, start: Int, before: Int, count: Int) {
                textInputLayout.error = ""
            }

            override fun afterTextChanged(textChanged: Editable?) {}

        }

    }

}