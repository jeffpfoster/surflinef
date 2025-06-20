package com.example.surflinekotlin.network

import com.example.surflinekotlin.data.TidesResponse // Corrected import
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TidesService {
    /**
     * Fetches tide forecasts for a specified spot.
     * The base URL is configured in the Retrofit client (ApiConstants.TIDES_BASE_URL).
     * The path for this endpoint is "tides".
     *
     * @param spotId The ID of the spot for which to fetch tide data.
     * @param days The number of days of forecast data to retrieve.
     * @param accessToken Optional access token for authentication.
     * @return A Retrofit Response object containing TidesResponse on success.
     */
    @GET("tides") // Path relative to TIDES_BASE_URL
    suspend fun getTides(
        @Query("spotId") spotId: String,
        @Query("days") days: Int,
        @Query("accesstoken") accessToken: String? // Nullable, as "omitempty" in Go struct's url tag
    ): Response<TidesResponse>
}
