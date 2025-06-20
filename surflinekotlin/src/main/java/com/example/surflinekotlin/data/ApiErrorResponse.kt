package com.example.surflinekotlin.data

import com.google.gson.annotations.SerializedName

/**
 * Represents a generic error response from the API.
 * The fields are based on common error patterns and may need adjustment
 * if the Surfline API uses a different general error structure.
 *
 * It's designed to capture various possible error fields. If the API uses a consistent
 * subset (e.g., always 'error' and 'message'), this can be simplified.
 */
data class ApiErrorResponse(
    @SerializedName("error") val error: String?,
    @SerializedName("message") val message: String?, // Often 'message' or 'error_description'
    @SerializedName("description") val description: String?,
    @SerializedName("error_description") val errorDescription: String?, // From ErrorLoginResponse, might be common
    @SerializedName("code") val code: Int? // API-specific error code if present in the body
)
