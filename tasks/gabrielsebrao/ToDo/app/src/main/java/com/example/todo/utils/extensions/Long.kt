package com.example.todo.utils.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDate(): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(this))