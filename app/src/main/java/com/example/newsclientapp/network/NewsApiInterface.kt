package com.example.newsclientapp.network

import com.example.newsclientapp.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiInterface {
    @GET("v4/articles/")
    suspend fun getTopNews(
        @Query("limit") limit: Int = 25,
        @Query("offset") offset: Int = 0
    ): Response<NewsResponse>
}