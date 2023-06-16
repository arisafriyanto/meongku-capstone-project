package com.example.meongku.api.article

data class Article(
    val id: String,
    val article_title: String,
    val article_body: String,
    val article_category: List<String>,
    val article_image: String,
    val created_at: CreatedAt
)

