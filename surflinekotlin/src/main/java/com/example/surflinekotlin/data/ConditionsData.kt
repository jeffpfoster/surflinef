package com.example.surflinekotlin.data

// Import for Units from CommonData.kt
import com.example.surflinekotlin.data.Units
import com.google.gson.annotations.SerializedName

data class ConditionsQuery(
    @SerializedName("subregionId") val subregionId: String,
    @SerializedName("days") val days: Int,
    @SerializedName("accesstoken") val accessToken: String? // omitempty means it can be absent
)

data class ConditionsResponse(
    @SerializedName("associated") val associated: ConditionsAssociated,
    @SerializedName("data") val data: ConditionsData // Changed from conditonsData
)

data class ConditionsAssociated(
    @SerializedName("units") val units: Units, // This will now refer to the imported Units
    @SerializedName("utcOffset") val utcOffset: Int // Go's int32 maps to Int in Kotlin
)

// Removed local Units definition

data class ConditionsData( // Renamed from conditonsData
    @SerializedName("conditions") val conditions: List<Condition>
)

data class Condition(
    @SerializedName("timestamp") val timestamp: Int,
    @SerializedName("forecaster") val forecaster: Forecaster,
    @SerializedName("human") val human: Boolean,
    @SerializedName("observation") val observation: String,
    @SerializedName("am") val am: Report,
    @SerializedName("pm") val pm: Report
)

data class Forecaster(
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val avatar: String
)

data class Report(
    @SerializedName("maxHeight") val maxHeight: Double,
    @SerializedName("minHeight") val minHeight: Double,
    @SerializedName("humanRelation") val humanRelation: String,
    @SerializedName("rating") val rating: String,
    @SerializedName("plus") val plus: Boolean,
    @SerializedName("occasionalHeight") val occasionalHeight: Double
)
