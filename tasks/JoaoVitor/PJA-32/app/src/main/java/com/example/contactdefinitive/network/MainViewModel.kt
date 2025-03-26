package com.example.contactdefinitive.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable

class MainViewModel: ViewModel() {

    private var disposable: Disposable? = null

    var reqSuccess = MutableLiveData(false)
    var availableCurrenciesMap: Map<String, String> = mapOf()

    var selectedFirstCurrency = MutableLiveData("Selecione uma moeda")
    var selectedSecCurrency = MutableLiveData("Selecione uma moeda")

    fun setAvailableCurrenciesMap() {
        disposable = getApiSingle().subscribe({ map ->
            availableCurrenciesMap = map
            Log.d("RX_DEBUG_HOME (ON SUCCESS)", "disposable is disposed: ${disposable?.isDisposed}, disposable location: ${System.identityHashCode(disposable)}")

            reqSuccess.postValue(true)
        }, { error ->
            Log.e("RX_DEBUG_HOME (ON ERROR)", error.toString())
            Log.e("RX_DEBUG_HOME (ON ERROR)", "disposable is disposed: ${disposable?.isDisposed}, disposable location: ${System.identityHashCode(disposable)}")
        })
    }

    private fun getApiSingle(): Single<Map<String, String>> {
        return NetworkUtils.getRetrofitInstance()
            .create(CurrencyClient::class.java)
            .getCurrencies()
            .map { responseBody ->
                val jsonString = responseBody.string()
                Log.d("RX_DEBUG_HOME_RESPONSE", jsonString)
                Moshi.getMapFromJson(jsonString) ?: emptyMap()
            }
    }
}

