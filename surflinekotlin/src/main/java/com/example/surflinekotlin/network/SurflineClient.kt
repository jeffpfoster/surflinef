package com.example.surflinekotlin.network

import com.example.surflinekotlin.data.ErrorLoginResponse // Corrected import: No LoginData prefix needed if classes are top-level
import com.example.surflinekotlin.data.ApiErrorResponse // New import
import com.example.surflinekotlin.data.ErrorLoginResponse
import com.example.surflinekotlin.data.LoginPayload
import com.example.surflinekotlin.data.LoginResponse
import com.example.surflinekotlin.data.ConditionsResponse
import com.example.surflinekotlin.data.TidesResponse
import com.example.surflinekotlin.data.WaveResponse
import com.example.surflinekotlin.data.Taxonomy // New import
import com.example.surflinekotlin.managers.TokenManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

// Sealed class for handling network results
sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(
        val code: Int,
        val message: String?,
        val loginErrorBody: ErrorLoginResponse? = null, // Specific to login
        val generalErrorBody: ApiErrorResponse? = null  // For other API errors
    ) : NetworkResult<Nothing>()
    data class Exception(val throwable: Throwable) : NetworkResult<Nothing>()
}

class SurflineClient(
    private val okHttpClient: OkHttpClient,
    private val tokenManager: TokenManager, // Added TokenManager
    private val gson: Gson = Gson() // Allow providing a custom Gson instance
) {

    // Retrofit instance for Authentication service
    private val authRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.LOGIN_BASE_URL)
            .client(okHttpClient) // Reuse the OkHttpClient
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // Lazy delegate for AuthService
    private val authService: AuthService by lazy {
        authRetrofit.create(AuthService::class.java)
    }

    /**
     * Attempts to log in a user with the given credentials.
     *
     * @param username The user's username.
     * @param password The user's password.
     * @param isShortLived Whether the requested token should be short-lived. Defaults to false.
     * @return A [NetworkResult] wrapping the [LoginResponse] on success, or an error/exception.
     */
    suspend fun login(
        username: String,
        password: String,
        isShortLived: Boolean = false // Default matches Go's LoginQuery behavior if not specified
    ): NetworkResult<LoginResponse> {
        // Construct the payload, including the static AuthorizationString as per Go's DefaultLoginPayload
        val payload = LoginPayload(
            authorizationString = "Basic NWM1OWU3YzNmMGI2Y2IxYWQwMmJhZjY2OnNrX1FxWEpkbjZOeTVzTVJ1MjdBbWcz",
            username = username,
            password = password,
            grantType = "password",    // From Go's DefaultLoginPayload
            forced = true,             // From Go's DefaultLoginPayload
            deviceId = "",             // From Go's DefaultLoginPayload, can be customized if needed
            deviceType = ""            // From Go's DefaultLoginPayload, can be customized if needed
        )

        return withContext(Dispatchers.IO) { // Perform network call on IO dispatcher
            try {
                val response = authService.login(isShortLived = isShortLived, payload = payload)
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse -> // Successfully got a response body
                        // Save tokens on successful login
                        tokenManager.saveTokens(loginResponse.accessToken, loginResponse.refreshToken)
                        NetworkResult.Success(loginResponse)
                    } ?: NetworkResult.Exception(Throwable("Login response body is null but request was successful")) // Should not happen with a 200
                } else { // HTTP error (4xx, 5xx)
                    val errorBodyString = response.errorBody()?.string() // Consume error body once
                    val parsedLoginError = errorBodyString?.let {
                        try {
                            gson.fromJson(it, ErrorLoginResponse::class.java)
                        } catch (e: Exception) {
                            // Failed to parse the error body into ErrorLoginResponse
                            null // Or log this specific parsing error
                        }
                    }
                    NetworkResult.Error(
                        code = response.code(),
                        message = response.message(),
                        loginErrorBody = parsedLoginError,
                        generalErrorBody = null // Not a general API error in this case
                    )
                }
            } catch (e: IOException) {
                // Network-related exceptions (e.g., no internet, host unreachable)
                NetworkResult.Exception(e)
            } catch (e: Exception) {
                // Other unexpected exceptions (e.g., from Retrofit, Gson, or other synchronous code)
                NetworkResult.Exception(e)
            }
        }
    }

    // Placeholder for other API service integrations (Conditions, Tides, Wave, Taxonomy)
    // These will be added in subsequent steps.

    // Retrofit instance for Conditions Service
    private val conditionsRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.CONDITIONS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // Lazy delegate for ConditionsService
    private val conditionsService: ConditionsService by lazy {
        conditionsRetrofit.create(ConditionsService::class.java)
    }

    // Retrofit instance for Tides Service
    private val tidesRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.TIDES_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // Lazy delegate for TidesService
    private val tidesService: TidesService by lazy {
        tidesRetrofit.create(TidesService::class.java)
    }

    // Retrofit instance for Wave Service
    private val waveRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.WAVE_BASE_URL) // Same as TIDES_BASE_URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // Lazy delegate for WaveService
    private val waveService: WaveService by lazy {
        waveRetrofit.create(WaveService::class.java)
    }

    // Retrofit instance for Taxonomy Service
    private val taxonomyRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.TAXONOMY_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // Lazy delegate for TaxonomyService
    private val taxonomyService: TaxonomyService by lazy {
        taxonomyRetrofit.create(TaxonomyService::class.java)
    }

    fun getAccessToken(): String? {
        return tokenManager.getAccessToken()
    }

    fun clearTokens() {
        tokenManager.clearTokens()
    }

    suspend fun getConditions(
        subregionId: String,
        days: Int
    ): NetworkResult<ConditionsResponse> {
        val accessToken = tokenManager.getAccessToken()
        // The accessToken is nullable and will be passed as such to the service.
        // The API is expected to handle it if it's optional (due to "omitempty" in Go).
        // If the token was strictly required, a check like:
        // if (accessToken == null) return NetworkResult.Error(401, "Access token missing", null)
        // could be implemented here.

        return withContext(Dispatchers.IO) {
            try {
                val response = conditionsService.getConditions(
                    subregionId = subregionId,
                    days = days,
                    accessToken = accessToken
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.Success(it)
                    } ?: NetworkResult.Exception(Throwable("Conditions response body is null but request was successful"))
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    val generalError = errorBodyString?.let {
                        try {
                            gson.fromJson(it, ApiErrorResponse::class.java)
                        } catch (e: Exception) {
                            null // Parsing failed
                        }
                    }
                    NetworkResult.Error(
                        code = response.code(),
                        message = response.message(),
                        loginErrorBody = null, // Not a login error
                        generalErrorBody = generalError
                    )
                }
            } catch (e: IOException) {
                // Network-related errors
                NetworkResult.Exception(e)
            } catch (e: Exception) {
                // Other unexpected errors
                NetworkResult.Exception(e)
            }
        }
    }

    suspend fun getTides(
        spotId: String,
        days: Int
    ): NetworkResult<TidesResponse> {
        val accessToken = tokenManager.getAccessToken()
        // Assuming API handles missing token if it's optional based on "omitempty"

        return withContext(Dispatchers.IO) {
            try {
                val response = tidesService.getTides(
                    spotId = spotId,
                    days = days,
                    accessToken = accessToken
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.Success(it)
                    } ?: NetworkResult.Exception(Throwable("Tides response body is null but request was successful"))
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    val generalError = errorBodyString?.let {
                        try {
                            gson.fromJson(it, ApiErrorResponse::class.java)
                        } catch (e: Exception) {
                            null // Parsing failed
                        }
                    }
                    NetworkResult.Error(
                        code = response.code(),
                        message = response.message(),
                        loginErrorBody = null, // Not a login error
                        generalErrorBody = generalError
                    )
                }
            } catch (e: IOException) {
                NetworkResult.Exception(e)
            } catch (e: Exception) {
                NetworkResult.Exception(e)
            }
        }
    }

    suspend fun getWave(
        spotId: String,
        days: Int,
        intervalHours: Int,
        maxHeights: Boolean
    ): NetworkResult<WaveResponse> {
        val accessToken = tokenManager.getAccessToken()
        // Assuming API handles missing token if it's optional

        return withContext(Dispatchers.IO) {
            try {
                val response = waveService.getWave(
                    spotId = spotId,
                    days = days,
                    intervalHours = intervalHours,
                    maxHeights = maxHeights,
                    accessToken = accessToken
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.Success(it)
                    } ?: NetworkResult.Exception(Throwable("Wave response body is null but request was successful"))
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    val generalError = errorBodyString?.let {
                        try {
                            gson.fromJson(it, ApiErrorResponse::class.java)
                        } catch (e: Exception) {
                            null // Parsing failed
                        }
                    }
                    NetworkResult.Error(
                        code = response.code(),
                        message = response.message(),
                        loginErrorBody = null, // Not a login error
                        generalErrorBody = generalError
                    )
                }
            } catch (e: IOException) {
                NetworkResult.Exception(e)
            } catch (e: Exception) {
                NetworkResult.Exception(e)
            }
        }
    }

    suspend fun getTaxonomy(
        id: String,
        maxDepth: Int,
        type: String
    ): NetworkResult<Taxonomy> {
        // No access token is used for this endpoint based on Go client analysis.
        return withContext(Dispatchers.IO) {
            try {
                val response = taxonomyService.getTaxonomy(
                    id = id,
                    maxDepth = maxDepth,
                    type = type
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.Success(it)
                    } ?: NetworkResult.Exception(Throwable("Taxonomy response body is null but request was successful"))
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    val generalError = errorBodyString?.let {
                        try {
                            gson.fromJson(it, ApiErrorResponse::class.java)
                        } catch (e: Exception) {
                            null // Parsing failed
                        }
                    }
                    NetworkResult.Error(
                        code = response.code(),
                        message = response.message(),
                        loginErrorBody = null, // Not a login error
                        generalErrorBody = generalError
                    )
                }
            } catch (e: IOException) {
                NetworkResult.Exception(e)
            } catch (e: Exception) {
                NetworkResult.Exception(e)
            }
        }
    }
}
