package com.example.todo.utils.converter

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Converter {

    private const val ONE_WEEK_DIFFERENCE = 1000 * 60 * 60 * 24 * 7
    private val BRAZILIAN_PORTUGUESE = Locale("pt", "BR")

    fun turnTimeStampToSpelledOut(conclusionTimestamp: Long, calendar: Calendar): String {

        var text = ""

        val date: ZonedDateTime = Instant.ofEpochMilli(conclusionTimestamp)
            .atZone(ZoneId.systemDefault())

        text += date.dayOfWeek.getDisplayName(TextStyle.FULL, BRAZILIAN_PORTUGUESE)
            .take(3)
            .plus(".")

        if(conclusionTimestamp > calendar.timeInMillis + ONE_WEEK_DIFFERENCE || conclusionTimestamp < calendar.timeInMillis)
            text += ", ${date.dayOfMonth} de ".plus(date.month.toString().take(3)
                .lowercase(BRAZILIAN_PORTUGUESE)
                .plus(".")
            )

        if(date.year != calendar.get(Calendar.YEAR))
            text += " ${date.year}"

        if(date.toLocalTime().minute < 10)
            text += " às ${date.toLocalTime().hour}h0${date.toLocalTime().minute}"
        else
            text += " às ${date.toLocalTime().hour}h${date.toLocalTime().minute}"

        return text

    }

}