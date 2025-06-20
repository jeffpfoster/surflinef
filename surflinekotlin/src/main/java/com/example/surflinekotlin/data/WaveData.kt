package com.example.surflinekotlin.data

import com.google.gson.annotations.SerializedName

// Assuming Units and Location are in the same package or imported correctly

data class WaveQuery(
    @SerializedName("spotId") val spotId: String,
    @SerializedName("days") val days: Int,
    @SerializedName("intervalHours") val intervalHours: Int,
    @SerializedName("maxHeights") val maxHeights: Boolean,
    @SerializedName("accesstoken") val accessToken: String? // omitempty in Go
)

data class WaveResponse(
    @SerializedName("associated") val associated: WaveAssociated,
    @SerializedName("data") val data: WaveData // Corresponds to waveData in Go
)

data class WaveAssociated(
    @SerializedName("units") val units: Units,
    @SerializedName("utcOffset") val utcOffset: Int, // Go's int32
    @SerializedName("location") val location: Location,
    @SerializedName("forecastLocation") val forecastLocation: Location,
    @SerializedName("offshoreLocation") val offshoreLocation: Location
)

data class WaveData( // Corresponds to waveData in Go
    @SerializedName("wave") val wave: List<WaveLeftAside>
)

data class WaveLeftAside( // Corresponds to Wave in Go
    @SerializedName("timestamp") val timestamp: Int,
    @SerializedName("surf") val surf: WaveSurf,
    @SerializedName("swells") val swells: List<Swell>
)

data class WaveSurf( // Corresponds to Surf in Go
    @SerializedName("min") val min: Double,
    @SerializedName("max") val max: Double,
    @SerializedName("optimalScore") val optimalScore: Int
)

data class Swell(
    @SerializedName("height") val height: Double,
    @SerializedName("period") val period: Int,
    @SerializedName("direction") val direction: Double,
    @SerializedName("directionMin") val directionMin: Double,
    @SerializedName("optimalScore") val optimalScore: Int
)
