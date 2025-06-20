package com.example.surflinekotlin.data

import com.google.gson.annotations.SerializedName

data class TaxonomyQuery(
    @SerializedName("id") val id: String,
    @SerializedName("maxDepth") val maxDepth: Int,
    @SerializedName("type") val type: String
)

data class Taxonomy(
    @SerializedName("_id") val _id: String,
    @SerializedName("spot") val spot: String?, // Nullable
    @SerializedName("subregion") val subregion: String?, // Nullable
    @SerializedName("type") val type: String,
    @SerializedName("name") val name: String,
    @SerializedName("hasSpots") val hasSpots: Boolean,
    @SerializedName("contains") val contains: List<Taxonomy> // Recursive definition
)
