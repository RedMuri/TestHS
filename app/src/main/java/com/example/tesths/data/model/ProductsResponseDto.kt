package com.example.tesths.data.model

import com.google.gson.annotations.SerializedName

data class ProductsResponseDto (
    @SerializedName("products") val products: List<ProductDto>,
)
