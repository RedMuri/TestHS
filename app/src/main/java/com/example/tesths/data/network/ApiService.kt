package com.example.tesths.data.network

import com.example.tesths.data.model.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("products")
    suspend fun getProducts(
        @Query(QUERY_PARAM_SKIP) skip: Int = 0,
        @Query(QUERY_PARAM_LIMIT) limit: Int = 40,
    ): ProductsResponseDto

    companion object {

        private const val QUERY_PARAM_SKIP = "skip"
        private const val QUERY_PARAM_LIMIT = "limit"
    }
}