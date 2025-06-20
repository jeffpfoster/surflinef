package com.example.surflinekotlin.managers

import android.content.Context
import android.content.SharedPreferences

// For production, consider using EncryptedSharedPreferences for better security.
// implementation "androidx.security:security-crypto:1.1.0-alpha06" (or latest version)
// val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
// val prefs = EncryptedSharedPreferences.create(
//     PREFS_NAME,
//     masterKeyAlias,
//     context,
//     EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//     EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
// )

class SharedPreferencesTokenManager(context: Context) : TokenManager {

    private val prefs: SharedPreferences

    init {
        // It's good practice to use applicationContext to avoid potential memory leaks
        // if the provided context is short-lived (e.g., an Activity context).
        val applicationContext = context.applicationContext
        prefs = applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private const val PREFS_NAME = "surfline_token_prefs"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    override fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .apply() // Use apply() for asynchronous save, commit() for synchronous.
    }

    override fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    override fun getRefreshToken(): String? {
        return prefs.getString(KEY_REFRESH_TOKEN, null)
    }

    override fun clearTokens() {
        prefs.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .apply()
    }
}
