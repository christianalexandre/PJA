package com.example.contactdefinitive

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.contactdefinitive.databinding.ActivitySecondBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var viewModel: ViewModelResultActivity
    private lateinit var binding: ActivitySecondBinding
    private var coinType1: String? = null
    private var coinType2: String? = null
    private var value: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider( this )[ViewModelResultActivity::class.java]
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getExtra()
        setupListener1()
        setupView()
    }

    private fun getExtra() {
        val bundle = intent.getBundleExtra("bundle")
        coinType1 = bundle?.getString("coinType1")
        coinType2 = bundle?.getString("coinType2")
        value = bundle?.getFloat("valor", 0f)
    }

    private fun setupListener1() {
        binding.buttonReturn.setOnClickListener {
            finish()
        }
    }

    private fun setupView() {
        with(binding) {

            fun result(c1 : String?, c2 : String?, v : Float?) {
                val result = viewModel.calculatingResult(c1, c2, v)
                val resultValue = String.format("%.2f", result)
                val completeValue = "$resultValue $coinType2"
                coin1.text = "$value $coinType1"
                coinResult.text = completeValue
            }

            fun imageView(x : ImageView, y : ImageView) {
                x.visibility = View.VISIBLE
                y.visibility = View.VISIBLE
            }

            when (value != null) {
                (coinType1 == "BRL" && coinType2 == "USD" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgBR1, imgUSD2)
                }
                (coinType1 == "BRL" && coinType2 == "GBP" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgBR1, imgGBP2)
                }
                (coinType1 == "BRL" && coinType2 == "CHF" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgBR1, imgCHF2)
                }
                (coinType1 == "BRL" && coinType2 == "EUR" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgBR1, imgEUR2)
                }
                (coinType1 == "USD" && coinType2 == "BRL" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgUSD1, imgBR2)
                }
                (coinType1 == "USD" && coinType2 == "GBP" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgUSD1, imgGBP2)
                }
                (coinType1 == "USD" && coinType2 == "CHF" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgUSD1, imgCHF2)
                }
                (coinType1 == "USD" && coinType2 == "EUR" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgUSD1, imgEUR2)
                }
                (coinType1 == "GBP" && coinType2 == "BRL" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgGBP1, imgBR2)
                }
                (coinType1 == "GBP" && coinType2 == "USD" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgGBP1, imgUSD2)
                }
                (coinType1 == "GBP" && coinType2 == "CHF" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgGBP1, imgCHF2)
                }
                (coinType1 == "GBP" && coinType2 == "EUR" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgGBP1, imgEUR2)
                }
                (coinType1 == "CHF" && coinType2 == "BRL" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgCHF1, imgBR2)
                }
                (coinType1 == "CHF" && coinType2 == "USD" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgCHF1, imgUSD2)
                }
                (coinType1 == "CHF" && coinType2 == "GBP" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgCHF1, imgGBP2)
                }
                (coinType1 == "CHF" && coinType2 == "EUR" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgCHF1, imgEUR2)
                }
                (coinType1 == "EUR" && coinType2 == "BRL" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgEUR1, imgBR2)
                }
                (coinType1 == "EUR" && coinType2 == "USD" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgEUR1, imgUSD2)
                }
                (coinType1 == "EUR" && coinType2 == "GBP" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgEUR1, imgGBP2)
                }
                (coinType1 == "EUR" && coinType2 == "CHF" && value != 0f) -> {
                    result(coinType1, coinType2, value)
                    imageView(imgEUR1, imgCHF2)
                }
                else -> {
                    print("error")
                }
            }
        }
    }
}