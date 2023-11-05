package com.example.tesths.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tesths.domain.usecases.GetProductsUseCase
import com.example.tesths.ui.state.ProductsScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductsViewModel @Inject constructor(
    val getProductsUseCase: GetProductsUseCase,
) : ViewModel() {

    private val _productsScreenState =
        MutableStateFlow<ProductsScreenState>(ProductsScreenState.Loading)
    val productsScreenState = _productsScreenState.asStateFlow()

    fun getProducts(shouldUpdate: Boolean = false) {
        viewModelScope.launch {
            getProductsUseCase()
                .onStart {
                    if (shouldUpdate) {
                        _productsScreenState.emit(ProductsScreenState.Loading)
                        delay(500)
                    }
                }
                .collect {
                    _productsScreenState.emit(it)
                }
        }
    }
}