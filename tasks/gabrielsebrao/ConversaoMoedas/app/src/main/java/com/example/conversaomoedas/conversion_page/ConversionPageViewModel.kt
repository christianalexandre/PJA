package com.example.conversaomoedas.conversion_page

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conversaomoedas.classes.currency.CurrencyApi
import com.example.conversaomoedas.classes.currency.CurrencyJsonItems
import com.example.conversaomoedas.classes.Moshi
import com.example.conversaomoedas.classes.RetrofitInstance
import com.example.conversaomoedas.classes.currency.Currency
import com.example.conversaomoedasapi.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class ConversionPageViewModel: ViewModel() {

    private lateinit var initialCurrency: Currency
    private lateinit var finalCurrency: Currency

    var convertedValue: Double = 0.0
    var finalValue: Double = 0.0
    var isLoading = MutableLiveData(true)
    var hasError = MutableLiveData(false)
    var conversionSuccess = MutableLiveData(false)
    private var disposable: Disposable? = null

    private lateinit var resources: Resources

    fun currencies(initialCurrency: Currency, finalCurrency: Currency): ConversionPageViewModel = apply {
        this.initialCurrency = initialCurrency.resources(resources)
        this.finalCurrency = finalCurrency.resources(resources)
    }

    fun resources(resources: Resources): ConversionPageViewModel = apply {
        this.resources = resources
    }

    fun convertValues(): Disposable? {

        disposable = getApiSingle(
            getEndpoint(
                initialCurrency.currencyCode,
                finalCurrency.currencyCode
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseMap ->
                val listResponse = responseMap.values.toList()

                initialCurrency.valueToDollar =
                    if(initialCurrency.currencyName == resources.getString(R.string.currency_american_dollar))
                        1.0
                    else
                        listResponse[0].bid.toDoubleOrNull() ?: 0.0

                finalCurrency.valueToDollar =
                    if(finalCurrency.currencyName == resources.getString(R.string.currency_american_dollar))
                        1.0
                    else if(initialCurrency.currencyName == resources.getString(R.string.currency_american_dollar))
                        listResponse[0].bid.toDoubleOrNull() ?: 0.0
                    else
                        listResponse[1].bid.toDoubleOrNull() ?: 0.0

                isLoading.postValue(false)
                conversionSuccess.postValue(true)

                Log.d("RX_DEBUG_CONVERSION (ON SUCCESS)", "disposable is disposed: ${disposable?.isDisposed}, disposable location: ${System.identityHashCode(disposable)}")
            }, { error ->

                isLoading.postValue(false)
                hasError.postValue(true)

                Log.d("RX_DEBUG_CONVERSION (ON ERROR)", "Error during conversion: $error")
            })

        return disposable

    }

    fun onSuccess() {

        convertedValue *= initialCurrency.valueToDollar
        finalValue = convertedValue / finalCurrency.valueToDollar
        disposable?.dispose()

    }

    private fun getApiSingle(endpoint: String): Single<Map<String, CurrencyJsonItems>> {

        return RetrofitInstance.getRetrofitInstance()
            .create(CurrencyApi::class.java)
            .getCurrencies(currencies = endpoint)
            .map { responseBody ->
                val jsonString = responseBody.string()
                Log.d("RX_DEBUG_CONVERSION_RESPONSE", jsonString)
                Moshi.getMapFromJson(jsonString) ?: emptyMap()
            }

    }

    private fun getEndpoint(firstCurrencyCode: String, finalCurrencyCode: String): String {

        var endpoint = ""

        if (firstCurrencyCode != resources.getString(R.string.currency_code_american_dollar))
            endpoint += "$firstCurrencyCode-usd,"

        if(finalCurrencyCode != resources.getString(R.string.currency_code_american_dollar) )
            endpoint += "$finalCurrencyCode-usd"
        else
            endpoint = endpoint.removeSuffix(",")

        Log.d("RX_ENDPOINT", "endpoint: $endpoint")
        return endpoint

    }

}