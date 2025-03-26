package com.example.conversaomoedas.classes.currency

import android.content.res.Resources
import android.os.Parcelable
import com.example.conversaomoedasapi.R
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class Currency(
    var currencyName: String = "",
    var currencyCode: String = "",
    var valueToDollar: Double = 0.0,
    var value: Double = 0.0
) : Parcelable {

    @IgnoredOnParcel
    private lateinit var resources: Resources

    fun resources(resources: Resources) = apply {
        this.resources = resources
    }

    fun setCurrencyCode() {

        if(currencyName == resources.getString(R.string.currency_default)) {
            currencyCode = "$"
            return
        }

        currencyCode = currencyName.replace(".*\\(".toRegex(), "").replace("\\)".toRegex(), "")

    }

}