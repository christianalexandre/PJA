package com.example.projetodojoao

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("debug", "onCreate: Acionado assim que o sistema cria a atividade")
    }

    override fun onStart() {
        super.onStart()
        Log.i("debug", "onStart: Quando a atividade entra no estado Iniciado")
    }

    override fun onResume() {
        super.onResume()
        Log.i("debug", "onResume: Quando a atividade entra no estado Retomado")
    }

    override fun onPause() {
        super.onPause()
        Log.i("debug", "onPause: Quando ocorre um evento de interrupção")
    }

    override fun onStop() {
        super.onStop()
        Log.i("debug", "onStop: Quando a atividade não está mais visível para o usuário")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("debug", "onDestroy: Quando a atividade é destruida")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("debug", "onRestart: Quando a atividade retorna do onStop() e retona ao onStart")
    }
}