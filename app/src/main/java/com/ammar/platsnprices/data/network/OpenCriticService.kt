package com.ammar.platsnprices.data.network

import com.ammar.platsnprices.data.entities.OpenCriticGame
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenCriticService {
    @GET("/api/game/{id}")
    suspend fun fetchGameData(@Path("id") id: Long): OpenCriticGame
}