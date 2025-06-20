package com.example.surflinekotlin.network

import com.example.surflinekotlin.data.WaveResponse // Corrected import
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WaveService {
    /**
     * Fetches wave forecasts for a specified spot.
     * The base URL is configured in the Retrofit client (ApiConstants.WAVE_BASE_URL).
     * The path for this endpoint is "wave".
     *
     * @param spotId The ID of the spot.
     * @param days Number of days for the forecast.
     * @param intervalHours Interval in hours between forecast points.
     * @param maxHeights Whether to include max height data.
     * @param accessToken Optional access token.
     * @return A Retrofit Response object containing WaveResponse on success.
     */
    @GET("wave") // Path relative to WAVE_BASE_URL
    suspend fun getWave(
        @Query("spotId") spotId: String,
        @Query("days") days: Int,
        @Query("intervalHours") intervalHours: Int,
        @Query("maxHeights") maxHeights: Boolean,
        @Query("accesstoken") accessToken: String? // Nullable, as "omitempty"
    ): Response<WaveResponse>
}
