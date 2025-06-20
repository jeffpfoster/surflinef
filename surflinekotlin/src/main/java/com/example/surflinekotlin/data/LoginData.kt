package com.example.surflinekotlin.data

import com.google.gson.annotations.SerializedName

data class LoginPayload(
    @SerializedName("authorizationString") val authorizationString: String,
    @SerializedName("device_id") val deviceId: String,
    @SerializedName("device_type") val deviceType: String,
    @SerializedName("forced") val forced: Boolean,
    @SerializedName("grant_type") val grantType: String,
    @SerializedName("password") val password: String,
    @SerializedName("username") val username: String
)

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("expires_in") val expiresIn: Long,
    @SerializedName("token_type") val tokenType: String
)

data class ErrorLoginResponse(
    @SerializedName("error") val error: String,
    @SerializedName("error_description") val errorDescription: String
)
