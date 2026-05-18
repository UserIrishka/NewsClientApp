package com.example.newsclientapp.model

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("results") val results: List<ArticleArticle>
)

data class ArticleArticle(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("summary") val summary: String?,
    @SerializedName("published_at") val publishedAt: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("url") val url: String
)
