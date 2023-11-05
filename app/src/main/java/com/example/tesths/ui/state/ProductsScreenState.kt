package com.example.tesths.ui.state

import com.example.tesths.domain.model.Product
import javax.inject.Inject

sealed class ProductsScreenState {
    object Loading : ProductsScreenState()
    data class Content @Inject constructor(val products: List<Product>) : ProductsScreenState()
    data class InternetError @Inject constructor(val products: List<Product>) : ProductsScreenState()
    data class Error @Inject constructor(val error: Throwable) : ProductsScreenState()
}