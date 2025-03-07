package com.example.todo.utils.converter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

object Converter {

    fun bitmapToByteArray(bitmap: Bitmap?, format: Bitmap.CompressFormat, quality: Int): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(format, quality, stream) ?: return null
        return stream.toByteArray()
    }

    fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray ?: return null, 0, byteArray.size)
    }

}