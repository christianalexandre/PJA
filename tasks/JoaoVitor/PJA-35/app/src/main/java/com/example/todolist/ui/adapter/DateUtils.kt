package com.example.todolist.ui.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun String?.toLocalDateTime(): LocalDateTime? {
    if (this.isNullOrBlank()) return null
    return try {
        val formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm") // Ajuste o formato se necessário
        LocalDateTime.parse(this, formatter)
    } catch (e: Exception) {
        null
    }
}
