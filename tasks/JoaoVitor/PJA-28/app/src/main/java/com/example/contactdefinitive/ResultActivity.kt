package com.example.contactdefinitive

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.contactdefinitive.databinding.ActivitySecondBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var viewModel: ResultViewModel
    private lateinit var binding: ActivitySecondBinding

    private var coinType1: String = ""
    private var coinType2: String = ""
    private var value: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider( this )[ResultViewModel::class.java]
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getExtra()
        setupListener()
        registerObservers()
        setupView()
    }

    private fun getExtra() {
        val bundle = intent.getBundleExtra("bundle")
        coinType1 = bundle?.getString("coinType1") ?: ""
        coinType2 = bundle?.getString("coinType2") ?: ""
        value = bundle?.getFloat("valor", 0f) ?: 0f
    }

    private fun setupListener() {
        binding.buttonReturn.setOnClickListener {
            finish()
        }
    }

    private fun registerObservers() {
        viewModel.resultLiveData.observe(this) {
            val valueF = "%,.2f".format(it)
            val text = "$valueF $coinType2"
            binding.coinResult.text = text

            binding.coin1.text = "$value $coinType1"
        }

        viewModel.resultErrorLiveData.observe(this) {
            Toast.makeText(this@ResultActivity, it, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setupView() {
        with(binding) {
            when (true) { (true) ->
                viewModel.getResult(coinType1, coinType2, value)
                else -> { print("error") }
            }

//            fun imageView(x : ImageView, y : ImageView) {
//                x.visibility = View.VISIBLE
//                y.visibility = View.VISIBLE
//            }

//            var image1 = FlagImage.BRL.getFlag
//            var image2 = FlagImage.USD.getFla

//            when (value != null) {
//                (coinType1 == "BRL" && coinType2 == "USD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgBR1, imgUSD2)
//                }
//                (coinType1 == "BRL" && coinType2 == "GBP" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgBR1, imgGBP2)
//                }
//                (coinType1 == "BRL" && coinType2 == "CHF" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgBR1, imgCHF2)
//                }
//                (coinType1 == "BRL" && coinType2 == "EUR" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgBR1, imgEUR2)
//                }
//                (coinType1 == "BRL" && coinType2 == "CNY" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgBR1, imgCNY2)
//                }
//                (coinType1 == "BRL" && coinType2 == "AUD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgBR1, imgAUD2)
//                }
//                (coinType1 == "BRL" && coinType2 == "SEK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgBR1, imgSEK2)
//                }
//                (coinType1 == "BRL" && coinType2 == "NOK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgBR1, imgNOK2)
//                }
//                (coinType1 == "BRL" && coinType2 == "MXN" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgBR1, imgMXN2)
//                }
//
//
//
//
//                (coinType1 == "USD" && coinType2 == "BRL" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgUSD1, imgBR2)
//                }
//                (coinType1 == "USD" && coinType2 == "GBP" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgUSD1, imgGBP2)
//                }
//                (coinType1 == "USD" && coinType2 == "CHF" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgUSD1, imgCHF2)
//                }
//                (coinType1 == "USD" && coinType2 == "EUR" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgUSD1, imgEUR2)
//                }
//                (coinType1 == "USD" && coinType2 == "CNY" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgUSD1, imgCNY2)
//                }
//                (coinType1 == "USD" && coinType2 == "AUD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgUSD1, imgAUD2)
//                }
//                (coinType1 == "USD" && coinType2 == "SEK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgUSD1, imgSEK2)
//                }
//                (coinType1 == "USD" && coinType2 == "NOK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgUSD1, imgNOK2)
//                }
//                (coinType1 == "USD" && coinType2 == "MXN" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgUSD1, imgMXN2)
//                }
//
//
//
//
//                (coinType1 == "GBP" && coinType2 == "BRL" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgGBP1, imgBR2)
//                }
//                (coinType1 == "GBP" && coinType2 == "USD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgGBP1, imgUSD2)
//                }
//                (coinType1 == "GBP" && coinType2 == "CHF" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgGBP1, imgCHF2)
//                }
//                (coinType1 == "GBP" && coinType2 == "EUR" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgGBP1, imgEUR2)
//                }
//                (coinType1 == "GBP" && coinType2 == "CNY" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgGBP1, imgCNY2)
//                }
//                (coinType1 == "GBP" && coinType2 == "AUD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgGBP1, imgAUD2)
//                }
//                (coinType1 == "GBP" && coinType2 == "SEK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgGBP1, imgSEK2)
//                }
//                (coinType1 == "GBP" && coinType2 == "NOK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgGBP1, imgNOK2)
//                }
//                (coinType1 == "GBP" && coinType2 == "MXN" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgGBP1, imgMXN2)
//                }
//
//
//
//
//                (coinType1 == "CHF" && coinType2 == "BRL" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCHF1, imgBR2)
//                }
//                (coinType1 == "CHF" && coinType2 == "USD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCHF1, imgUSD2)
//                }
//                (coinType1 == "CHF" && coinType2 == "GBP" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCHF1, imgGBP2)
//                }
//                (coinType1 == "CHF" && coinType2 == "EUR" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCHF1, imgEUR2)
//                }
//                (coinType1 == "CHF" && coinType2 == "CNY" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCHF1, imgCNY2)
//                }
//                (coinType1 == "CHF" && coinType2 == "AUD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCHF1, imgAUD2)
//                }
//                (coinType1 == "CHF" && coinType2 == "SEK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCHF1, imgSEK2)
//                }
//                (coinType1 == "CHF" && coinType2 == "NOK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCHF1, imgNOK2)
//                }
//                (coinType1 == "CHF" && coinType2 == "MXN" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCHF1, imgMXN2)
//                }
//
//
//
//
//                (coinType1 == "EUR" && coinType2 == "BRL" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgEUR1, imgBR2)
//                }
//                (coinType1 == "EUR" && coinType2 == "USD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgEUR1, imgUSD2)
//                }
//                (coinType1 == "EUR" && coinType2 == "GBP" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgEUR1, imgGBP2)
//                }
//                (coinType1 == "EUR" && coinType2 == "CHF" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgEUR1, imgCHF2)
//                }
//                (coinType1 == "EUR" && coinType2 == "CNY" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgEUR1, imgCNY2)
//                }
//                (coinType1 == "EUR" && coinType2 == "AUD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgEUR1, imgAUD2)
//                }
//                (coinType1 == "EUR" && coinType2 == "SEK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgEUR1, imgSEK2)
//                }
//                (coinType1 == "EUR" && coinType2 == "NOK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgEUR1, imgNOK2)
//                }
//                (coinType1 == "EUR" && coinType2 == "MXN" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgEUR1, imgMXN2)
//                }
//
//
//
//
//                (coinType1 == "CNY" && coinType2 == "BRL" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCNY1, imgBR2)
//                }
//                (coinType1 == "CNY" && coinType2 == "USD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCNY1, imgUSD2)
//                }
//                (coinType1 == "CNY" && coinType2 == "GBP" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCNY1, imgGBP2)
//                }
//                (coinType1 == "CNY" && coinType2 == "CHF" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCNY1, imgCHF2)
//                }
//                (coinType1 == "CNY" && coinType2 == "EUR" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCNY1, imgEUR2)
//                }
//                (coinType1 == "CNY" && coinType2 == "AUD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCNY1, imgAUD2)
//                }
//                (coinType1 == "CNY" && coinType2 == "SEK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCNY1, imgSEK2)
//                }
//                (coinType1 == "CNY" && coinType2 == "NOK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCNY1, imgNOK2)
//                }
//                (coinType1 == "CNY" && coinType2 == "MXN" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgCNY1, imgMXN2)
//                }
//
//
//
//
//                (coinType1 == "AUD" && coinType2 == "BRL" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgAUD1, imgBR2)
//                }
//                (coinType1 == "AUD" && coinType2 == "USD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgAUD1, imgUSD2)
//                }
//                (coinType1 == "AUD" && coinType2 == "GBP" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgAUD1, imgGBP2)
//                }
//                (coinType1 == "AUD" && coinType2 == "CHF" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgAUD1, imgCHF2)
//                }
//                (coinType1 == "AUD" && coinType2 == "CNY" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgAUD1, imgCNY2)
//                }
//                (coinType1 == "AUD" && coinType2 == "EUR" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgAUD1, imgEUR2)
//                }
//                (coinType1 == "AUD" && coinType2 == "SEK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgAUD1, imgSEK2)
//                }
//                (coinType1 == "AUD" && coinType2 == "NOK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgAUD1, imgNOK2)
//                }
//                (coinType1 == "AUD" && coinType2 == "MXN" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgAUD1, imgEUR2)
//                }
//
//
//
//
//                (coinType1 == "SEK" && coinType2 == "BRL" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgSEK1, imgBR2)
//                }
//                (coinType1 == "SEK" && coinType2 == "USD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgSEK1, imgUSD2)
//                }
//                (coinType1 == "SEK" && coinType2 == "GBP" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgSEK1, imgGBP2)
//                }
//                (coinType1 == "SEK" && coinType2 == "CHF" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgSEK1, imgCHF2)
//                }
//                (coinType1 == "SEK" && coinType2 == "CNY" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgSEK1, imgCNY2)
//                }
//                (coinType1 == "SEK" && coinType2 == "AUD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgSEK1, imgAUD2)
//                }
//                (coinType1 == "SEK" && coinType2 == "EUR" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgSEK1, imgEUR2)
//                }
//                (coinType1 == "SEK" && coinType2 == "NOK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgSEK1, imgNOK2)
//                }
//                (coinType1 == "SEK" && coinType2 == "MXN" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgSEK1, imgMXN2)
//                }
//
//
//
//
//                (coinType1 == "NOK" && coinType2 == "BRL" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgNOK1, imgBR2)
//                }
//                (coinType1 == "NOK" && coinType2 == "USD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgNOK1, imgUSD2)
//                }
//                (coinType1 == "NOK" && coinType2 == "GBP" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgNOK1, imgGBP2)
//                }
//                (coinType1 == "NOK" && coinType2 == "CHF" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgNOK1, imgCHF2)
//                }
//                (coinType1 == "NOK" && coinType2 == "CNY" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgNOK1, imgCNY2)
//                }
//                (coinType1 == "NOK" && coinType2 == "AUD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgNOK1, imgAUD2)
//                }
//                (coinType1 == "NOK" && coinType2 == "SEK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgNOK1, imgSEK2)
//                }
//                (coinType1 == "NOK" && coinType2 == "EUR" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgNOK1, imgEUR2)
//                }
//                (coinType1 == "NOK" && coinType2 == "MXN" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgNOK1, imgMXN2)
//                }
//
//
//
//
//                (coinType1 == "MXN" && coinType2 == "BRL" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgMXN1, imgBR2)
//                }
//                (coinType1 == "MXN" && coinType2 == "USD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgMXN1, imgUSD2)
//                }
//                (coinType1 == "MXN" && coinType2 == "GBP" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgMXN1, imgGBP2)
//                }
//                (coinType1 == "MXN" && coinType2 == "CHF" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgMXN1, imgCHF2)
//                }
//                (coinType1 == "MXN" && coinType2 == "CNY" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgMXN1, imgCNY2)
//                }
//                (coinType1 == "MXN" && coinType2 == "AUD" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgMXN1, imgAUD2)
//                }
//                (coinType1 == "MXN" && coinType2 == "SEK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgMXN1, imgSEK2)
//                }
//                (coinType1 == "MXN" && coinType2 == "NOK" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgMXN1, imgNOK2)
//                }
//                (coinType1 == "MXN" && coinType2 == "EUR" && value != 0f) -> {
//                    viewModel.getResult(coinType1, coinType2, value)
//                    imageView(imgMXN1, imgEUR2)
//                }
//
//                else -> {
//                    print("error")
//                }
//            }
//        }
        }
    }
}