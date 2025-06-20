package com.example.surflinekotlin.network

import com.example.surflinekotlin.data.Taxonomy // Corrected import
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TaxonomyService {
    /**
     * Fetches taxonomy data based on ID, max depth, and type.
     * The base URL is configured in the Retrofit client (ApiConstants.TAXONOMY_BASE_URL),
     * which should be "https://services.surfline.com/".
     * The path for this endpoint is "taxonomy".
     *
     * @param id The ID of the taxonomy entity.
     * @param maxDepth The maximum depth for nested entities.
     * @param type The type of taxonomy entity.
     * @return A Retrofit Response object containing Taxonomy on success.
     *         Note: The Go client's TaxonomyBaseURL was "https://services.surfline.com/taxonomy",
     *         implying no further path segment if that full URL is used as Retrofit's base.
     *         However, if TAXONOMY_BASE_URL is "https://services.surfline.com/", then @GET("taxonomy") is correct.
     *         This implementation assumes TAXONOMY_BASE_URL = "https://services.surfline.com/".
     */
    @GET("taxonomy") // Path relative to TAXONOMY_BASE_URL
    suspend fun getTaxonomy(
        @Query("id") id: String,
        @Query("maxDepth") maxDepth: Int,
        @Query("type") type: String
        // No access token for this endpoint as per v2/taxonomy.go
    ): Response<Taxonomy> // Note: Returns Taxonomy directly, not wrapped in a "Response" object like other data types
}
