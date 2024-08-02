package com.example.aplicativo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.aplicativo.ui.theme.AplicativoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("debug", "onCreate: onCreate é chamado quando a atividade é iniciada")
    }

    override fun onStart() {
        super.onStart()
        Log.d("debug", "onStart: onStart vem logo após o onCreate, tornando a atividade visível")
    }

    override fun onResume() {
        super.onResume()
        Log.d("debug", "onResume: onResume vem logo após o onStart, tornando a atividade interativa e o foco principal")
    }

    override fun onPause() {
        super.onPause()
        Log.d("debug", "onPause: onPause é chamado quando o usuário sai da atividade")
    }

    override fun onStop() {
        super.onStop()
        Log.d("debug", "onStop: onStop é chamado quando a atividade não está mais visível")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("debug", "onDestroy: onDestroy é chamado logo antes da atividade ser destruída")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("debug", "onRestart: onRestart é quando o usuário volta a visualizar a atividade (depois do onStop)")
    }
}