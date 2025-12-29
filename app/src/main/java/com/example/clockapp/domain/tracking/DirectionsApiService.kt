package com.example.clockapp.domain.tracking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface DirectionsApiService {
    @GET("maps/api/directions/json" )
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): Response<DirectionsResponse> // DirectionsResponse is your data class
}