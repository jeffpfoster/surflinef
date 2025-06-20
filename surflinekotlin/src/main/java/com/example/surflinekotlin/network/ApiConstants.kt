package com.example.surflinekotlin.network

object ApiConstants {
    // Base URL for login requests. Path ("trusted/token") is defined in AuthService.
    const val LOGIN_BASE_URL = "https://services.surfline.com/"

    // Base URL for conditions requests. Path ("conditions") will be defined in a ConditionsService.
    const val CONDITIONS_BASE_URL = "https://services.surfline.com/kbyg/regions/forecasts/"

    // Base URL for tides requests. Path ("tides") will be defined in a TidesService.
    const val TIDES_BASE_URL = "https://services.surfline.com/kbyg/spots/forecasts/"

    // Base URL for wave requests. Path ("wave") will be defined in a WaveService.
    // This is the same as TIDES_BASE_URL.
    const val WAVE_BASE_URL = "https://services.surfline.com/kbyg/spots/forecasts/"

    // Base URL for taxonomy requests. Path ("taxonomy") will be defined in a TaxonomyService.
    const val TAXONOMY_BASE_URL = "https://services.surfline.com/"
}
