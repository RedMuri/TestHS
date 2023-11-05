package com.example.tesths.domain

import com.example.tesths.ui.state.ProductsScreenState
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getProducts(): Flow<ProductsScreenState>
}