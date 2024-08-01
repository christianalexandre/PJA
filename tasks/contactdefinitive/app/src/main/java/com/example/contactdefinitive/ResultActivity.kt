package com.example.contactdefinitive

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.contactdefinitive.databinding.ActivitySecondBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListener1()
    }

    private fun setupListener1() {
        val bundle = intent.getBundleExtra("bundle")
        val coinValue1 = bundle?.getString("coinType1")
        val coinValue2 = bundle?.getString("coinType2")
        val valor: Float? = bundle?.getFloat("valor", 0f)

        when (valor != null) {

            (coinValue1 == "BRL" && coinValue2 == "USD" && valor != 0f) -> with(binding) {
                val resultado = (valor!! / 5.30f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgBR1.visibility = View.VISIBLE
                imgUSD2.visibility = View.VISIBLE
            }

            (coinValue1 == "BRL" && coinValue2 == "GBP" && valor != 0f) -> with(binding) {
                val resultado = (valor!! / 6.74f)
                coin1.text = "$valor $coinValue1"
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coinResult.text = completeValue
                imgBR1.visibility = View.VISIBLE
                imgGBP2.visibility = View.VISIBLE
            }

            (coinValue1 == "BRL" && coinValue2 == "CHF" && valor != 0f) -> with(binding) {
                val resultado = (valor!! / 5.91f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgBR1.visibility = View.VISIBLE
                imgCHF2.visibility = View.VISIBLE
            }

            (coinValue1 == "BRL" && coinValue2 == "EUR" && valor != 0f) -> with(binding) {
                val resultado = (valor!! / 5.72f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgBR1.visibility = View.VISIBLE
                imgEUR2.visibility = View.VISIBLE
            }

            (coinValue1 == "USD" && coinValue2 == "BRL" && valor != 0f) -> with(binding) {
                val resultado = (valor!! * 5.3f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgUSD1.visibility = View.VISIBLE
                imgBR2.visibility = View.VISIBLE
            }

            (coinValue1 == "USD" && coinValue2 == "GBP" && valor != 0f) -> with(binding) {
                val resultado = (valor!! / 1.44f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgUSD1.visibility = View.VISIBLE
                imgGBP2.visibility = View.VISIBLE
            }

            (coinValue1 == "USD" && coinValue2 == "CHF" && valor != 0f) -> with(binding) {
                val resultado = (valor!! / 0.61f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgUSD1.visibility = View.VISIBLE
                imgCHF2.visibility = View.VISIBLE
            }

            (coinValue1 == "USD" && coinValue2 == "EUR" && valor != 0f) -> with(binding) {
                val resultado = (valor!! / 0.42f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgUSD1.visibility = View.VISIBLE
                imgEUR2.visibility = View.VISIBLE
            }

            (coinValue1 == "GBP" && coinValue2 == "BRL" && valor != 0f) -> with(binding) {
                val resultado = (valor!! * 6.74f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgGBP1.visibility = View.VISIBLE
                imgBR2.visibility = View.VISIBLE
            }

            (coinValue1 == "GBP" && coinValue2 == "USD" && valor != 0f) -> with(binding) {
                val resultado = (valor!! * 1.44f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgGBP1.visibility = View.VISIBLE
                imgUSD2.visibility = View.VISIBLE
            }

            (coinValue1 == "GBP" && coinValue2 == "CHF" && valor != 0f) -> with(binding) {
                val resultado = (valor!! * 0.83f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgGBP1.visibility = View.VISIBLE
                imgCHF2.visibility = View.VISIBLE
            }

            (coinValue1 == "GBP" && coinValue2 == "EUR" && valor != 0f) -> with(binding) {
                val resultado = (valor!! * 1.02f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgGBP1.visibility = View.VISIBLE
                imgEUR2.visibility = View.VISIBLE
            }

            (coinValue1 == "CHF" && coinValue2 == "BRL" && valor != 0f) -> with(binding) {
                val resultado = (valor!! * 5.91f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgCHF1.visibility = View.VISIBLE
                imgBR2.visibility = View.VISIBLE
            }

            (coinValue1 == "CHF" && coinValue2 == "USD" && valor != 0f) -> with(binding) {
                val resultado = (valor!! * 0.61f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgCHF1.visibility = View.VISIBLE
                imgUSD2.visibility = View.VISIBLE
            }

            (coinValue1 == "CHF" && coinValue2 == "GBP" && valor != 0f) -> with(binding) {
                val resultado = (valor!! / 0.83f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgCHF1.visibility = View.VISIBLE
                imgGBP2.visibility = View.VISIBLE
            }

            (coinValue1 == "CHF" && coinValue2 == "EUR" && valor != 0f) -> with(binding) {
                val resultado = (valor!! * 0.19f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgCHF1.visibility = View.VISIBLE
                imgEUR2.visibility = View.VISIBLE
            }

            (coinValue1 == "EUR" && coinValue2 == "BRL" && valor != 0f) -> with(binding) {
                val resultado = (valor!! * 5.72f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgEUR1.visibility = View.VISIBLE
                imgBR2.visibility = View.VISIBLE
            }

            (coinValue1 == "EUR" && coinValue2 == "USD" && valor != 0f) -> with(binding) {
                val resultado = (valor!! * 0.42f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgEUR1.visibility = View.VISIBLE
                imgUSD2.visibility = View.VISIBLE
            }

            (coinValue1 == "EUR" && coinValue2 == "GBP" && valor != 0f) -> with(binding) {
                val resultado = (valor!! / 1.02f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgEUR1.visibility = View.VISIBLE
                imgGBP2.visibility = View.VISIBLE
            }

            (coinValue1 == "EUR" && coinValue2 == "CHF" && valor != 0f) -> with(binding) {
                val resultado = (valor!! / 0.19f)
                val resultValue = String.format("%.2f", resultado)
                val completeValue = "$resultValue $coinValue2"
                coin1.text = "$valor $coinValue1"
                coinResult.text = completeValue
                imgEUR1.visibility = View.VISIBLE
                imgCHF2.visibility = View.VISIBLE
            }

            (coinValue1 == "Selecionar" || coinValue2 == "Selecionar") -> with(binding) {
                Toast.makeText(this@ResultActivity, R.string.selected_close_toast, Toast.LENGTH_SHORT).show()
                finish()
            }

            (coinValue1 == coinValue2) -> with(binding) {
                Toast.makeText(this@ResultActivity, R.string.required_distint_coins_toast, Toast.LENGTH_SHORT).show()
                finish()
            }

            else -> with(binding) {
                Toast.makeText(this@ResultActivity, R.string.required_value_toast, Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.buttonRetornar.setOnClickListener {
            finish()
        }
    }
}