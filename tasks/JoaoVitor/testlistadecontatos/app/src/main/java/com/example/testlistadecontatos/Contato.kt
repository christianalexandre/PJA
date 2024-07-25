package com.example.testlistadecontatos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contato(
    val name: String,
    val num: String
) : Parcelable
