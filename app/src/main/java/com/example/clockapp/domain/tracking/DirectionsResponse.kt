package com.example.clockapp.domain.tracking

import com.google.gson.annotations.SerializedName


data class DirectionsResponse(
    val routes: List<Route>,
    val status: String
)

data class Route(
    @SerializedName("overview_polyline")
    val overviewPolyline: Polyline
)

data class Polyline(
    val points: String
)
