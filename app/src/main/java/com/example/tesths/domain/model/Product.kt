package com.example.tesths.domain.model

import javax.inject.Inject


data class Product @Inject constructor(
    val id: Int,
    val title: String,
    val price: Int,
    val image: String,
    val description: String,
)
