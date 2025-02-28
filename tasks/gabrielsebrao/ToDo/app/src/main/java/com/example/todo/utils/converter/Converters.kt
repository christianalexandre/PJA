package com.example.todo.utils.converter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Base64.DEFAULT
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

object Converters{

    @TypeConverter
    fun bitmapToByteArray(
        bitmap: Bitmap?,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = 100
    ): ByteArray {

        val outputStream = ByteArrayOutputStream()

        bitmap?.compress(format, quality, outputStream)
        return outputStream.toByteArray()

    }

    @TypeConverter
    fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap =
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size ?: 0)

}