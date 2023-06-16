package com.example.meongku.api.CatFoodRecommendation

data class CatFoodRecommendationRequest(
    val nameInput: String,
    val foodTypeInput: String,
    val ageInput: Int,
    val activityInput: String,
    val raceInput: String
)
