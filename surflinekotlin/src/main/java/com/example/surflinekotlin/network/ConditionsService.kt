package com.example.surflinekotlin.network

import com.example.surflinekotlin.data.ConditionsResponse // Corrected import path
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ConditionsService {
    /**
     * Fetches conditions for a specified subregion.
     * The base URL is configured in the Retrofit client (ApiConstants.CONDITIONS_BASE_URL).
     * The path for this endpoint is "conditions".
     *
     * @param subregionId The ID of the subregion for which to fetch conditions.
     * @param days The number of days of forecast data to retrieve.
     * @param accessToken Optional access token for authentication. The API handles missing/empty token if it's optional.
     * @return A Retrofit Response object containing ConditionsResponse on success.
     */
    @GET("conditions") // Path relative to CONDITIONS_BASE_URL
    suspend fun getConditions(
        @Query("subregionId") subregionId: String,
        @Query("days") days: Int,
        @Query("accesstoken") accessToken: String? // Nullable, as "omitempty" in Go struct's url tag
    ): Response<ConditionsResponse>
}
