package com.example.contactdefinitive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contactdefinitive.network.NetworkClientHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ResultViewModel : ViewModel() {

    private var disposable: Disposable? = null

    private val _resultLiveData: MutableLiveData<Float> = MutableLiveData()
    val resultLiveData: LiveData<Float> = _resultLiveData

    private val _resultErrorLiveData: MutableLiveData<String> = MutableLiveData()
    val resultErrorLiveData: LiveData<String> = _resultErrorLiveData

    private fun returnRate(a: Float, b: Float): Float {
        val rate = a / b
        return rate
    }

    fun getResult(coinType1: String, coinType2: String, value: Float) {
        disposable?.dispose()
        var endpoint = ""

        if (coinType1 != "BRL") {
            endpoint += "$coinType1,"
        }
        if (coinType2 != "BRL") {
            endpoint += coinType2
        } else {
            endpoint = endpoint.removeSuffix(",")
        }

        disposable = NetworkClientHelper.apiClient.getCurrencyRate(endpoint)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ resultModel: ResultModel ->
                disposable?.dispose()
                var rate1 = resultModel.firstCurrencies["${coinType1}BRL"]?.coinValue?.toFloat()
                var rate2 = resultModel.secondCurrencies["${coinType2}BRL"]?.coinValue?.toFloat()

                if (rate1 == null) {
                    rate1 = value
                }

                if (rate2 == null) {
                    rate2 = 1f
                }

                val rate = returnRate(rate1, rate2)
                _resultLiveData.value = value * rate

            }, {
                disposable?.dispose()
                println("error = ${it.message}")
                _resultErrorLiveData.value = it.message
            })
    }
}
