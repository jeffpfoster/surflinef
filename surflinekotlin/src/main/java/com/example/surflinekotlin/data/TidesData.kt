package com.example.surflinekotlin.data

import com.google.gson.annotations.SerializedName

// Assuming Units and Location are in the same package or imported correctly
// import com.example.surflinekotlin.data.Units // Already in this package
// import com.example.surflinekotlin.data.Location // Already in this package

data class TidesQuery(
    @SerializedName("spotId") val spotId: String,
    @SerializedName("days") val days: Int,
    @SerializedName("accesstoken") val accessToken: String? // omitempty in Go
)

data class TidesResponse(
    @SerializedName("associated") val associated: TidesAssociated,
    @SerializedName("data") val data: TidesData // Corresponds to tidesData in Go
)

data class TidesAssociated(
    @SerializedName("units") val units: Units,
    @SerializedName("utcOffset") val utcOffset: Int, // Go's int32
    @SerializedName("tideLocation") val tideLocation: Location
)

data class TidesData( // Corresponds to tidesData in Go
    @SerializedName("tides") val tides: List<Tide>
)

data class Tide(
    @SerializedName("timestamp") val timestamp: Int,
    @SerializedName("type") val type: String,
    @SerializedName("height") val height: Double
)
