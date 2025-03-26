package com.example.todo.utils.extensions

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val ONE_WEEK_DIFFERENCE = 1000 * 60 * 60 * 24 * 7
private val BRAZILIAN_PORTUGUESE = Locale("pt", "BR")

fun Long.toDate(): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(this))

fun Long.toSpelledOut(calendar: Calendar): String {

    var text = ""

    val date: ZonedDateTime = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())

    text += if(date.dayOfMonth == calendar.get(Calendar.DAY_OF_MONTH))
        "hoje"
    else
        date.dayOfWeek.getDisplayName(TextStyle.FULL, BRAZILIAN_PORTUGUESE)
        .take(3)
        .plus(".")

    if((this > calendar.timeInMillis + ONE_WEEK_DIFFERENCE || this < calendar.timeInMillis) && date.dayOfMonth != calendar.get(Calendar.DAY_OF_MONTH))
        text += ", ${date.dayOfMonth} de ".plus(date.month.getDisplayName(TextStyle.FULL, BRAZILIAN_PORTUGUESE).take(3)
            .lowercase(BRAZILIAN_PORTUGUESE)
            .plus(".")
        )

    if(date.year != calendar.get(Calendar.YEAR))
        text += " ${date.year}"

    text += if(date.toLocalTime().minute < 10)
        " às ${date.toLocalTime().hour}h0${date.toLocalTime().minute}"
    else
        " às ${date.toLocalTime().hour}h${date.toLocalTime().minute}"

    return text

}