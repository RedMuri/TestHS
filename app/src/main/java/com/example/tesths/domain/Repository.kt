package com.example.tesths.domain

import com.example.tesths.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getProducts(skip: Int,limit: Int): Flow<List<Product>>
}