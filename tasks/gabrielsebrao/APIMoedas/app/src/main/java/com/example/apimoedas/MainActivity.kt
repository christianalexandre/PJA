package com.example.apimoedas

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.apimoedas.databinding.MainActivityBinding
import com.example.apimoedas.Api.CallApi
import com.example.apimoedas.Connection.Connection

@SuppressLint("StaticFieldLeak")
lateinit var binding: MainActivityBinding
private var connection = Connection()
private var callApi = CallApi()

class MainActivity : ComponentActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!connection.checkForInternet(this)) {
            Toast.makeText(this, "Sem internet!", Toast.LENGTH_LONG).show()
            binding.currencyOne.text = "Conecta-se com a Internet e reinicie o app!"
        } else {
            callApi.callApi(this)
        }
    }
}