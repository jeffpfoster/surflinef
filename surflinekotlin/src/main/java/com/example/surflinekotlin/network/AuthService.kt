package com.example.surflinekotlin.network

import com.example.surflinekotlin.data.LoginPayload // Corrected import
import com.example.surflinekotlin.data.LoginResponse // Corrected import
import retrofit2.Response
import retrofit2.http.Body
// import retrofit2.http.Headers // Removed as Authorization will be in body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    /**
     * Performs a login request to the Surfline API.
     * The base URL (https://services.surfline.com/) should be configured in the Retrofit client.
     * The AuthorizationString is part of the LoginPayload body.
     *
     * @param isShortLived If true, the token may have a shorter validity period.
     * @param payload The login credentials and device information, including AuthorizationString.
     * @return A Retrofit Response object containing LoginResponse on success,
     *         or allowing access to the error body on failure.
     */
    // @Headers("Authorization: Basic NWM1OWU3YzNmMGI2Y2IxYWQwMmJhZjY2OnNrX1FxWEpkbjZOeTVzTVJ1MjdBbWcz") // Removed
    @POST("trusted/token") // Path relative to base URL (e.g., https://services.surfline.com/)
    suspend fun login(
        @Query("isShortLived") isShortLived: Boolean,
        @Body payload: LoginPayload
    ): Response<LoginResponse>
}
