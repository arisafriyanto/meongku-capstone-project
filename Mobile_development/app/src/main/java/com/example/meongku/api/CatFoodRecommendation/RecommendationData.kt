package com.example.meongku.api.CatFoodRecommendation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecommendationData(
    val id: Int,
    val name: String,
    val brand: String,
    val productName: String,
    val race: String,
    val productLink: String
): Parcelable
