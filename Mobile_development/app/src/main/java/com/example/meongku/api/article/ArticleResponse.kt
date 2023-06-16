package com.example.meongku.api.article

data class ArticleResponse(
    val statusCode: Int,
    val status: String,
    val message: String,
    val articles: List<Article>
)
