package com.example.meongku.api.CatFoodRecommendation

data class CatFoodRecommendationResponse(
    val statusCode: Int,
    val status: String,
    val message: String,
    val data: RecommendationData
)
