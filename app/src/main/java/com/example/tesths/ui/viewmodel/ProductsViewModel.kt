package com.example.tesths.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tesths.data.Mapper
import com.example.tesths.data.RepositoryImpl
import com.example.tesths.data.network.ApiFactory
import com.example.tesths.domain.usecases.GetProductsUseCase
import com.example.tesths.ui.state.ProductsScreenState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductsViewModel @Inject constructor(
    val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _productsScreenState =
        MutableStateFlow<ProductsScreenState>(ProductsScreenState.Loading)
    val productsScreenState = _productsScreenState.asStateFlow()

    fun getProducts(skip: Int = 0, limit: Int = 10) {
        viewModelScope.launch {
            getProductsUseCase(skip, limit)
                .onStart {
                    _productsScreenState.emit(ProductsScreenState.Loading)
                }
                .collect {
                    _productsScreenState.emit(it as ProductsScreenState)
                }
        }
    }
}