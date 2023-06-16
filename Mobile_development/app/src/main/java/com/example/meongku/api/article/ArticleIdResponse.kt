package com.example.meongku.api.article

data class ArticleIdResponse(
    val statusCode: Int,
    val status: String,
    val message: String,
    val article: Article
)
