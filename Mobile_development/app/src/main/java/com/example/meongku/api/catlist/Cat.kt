package com.example.meongku.api.catlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Cat(
    val id: Int,
    val race: String,
    val desc: String,
    val photo: String
) : Parcelable