package com.example.surflinekotlin.data

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?
)

data class Units(
    @SerializedName("temperature") val temperature: String?,
    @SerializedName("waveHeight") val waveHeight: String?,
    @SerializedName("windSpeed") val windSpeed: String?,
    @SerializedName("tideHeight") val tideHeight: String?
)
