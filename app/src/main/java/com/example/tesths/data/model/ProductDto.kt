package com.example.tesths.data.model

import com.google.gson.annotations.SerializedName

data class ProductDto (
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Int,
    @SerializedName("thumbnail") val image: String,
)
