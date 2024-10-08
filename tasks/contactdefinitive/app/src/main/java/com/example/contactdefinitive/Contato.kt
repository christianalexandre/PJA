package com.example.contactdefinitive

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val name: String,
    val num: String
) : Parcelable