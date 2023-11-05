package com.example.tesths.data

import com.example.tesths.data.network.ApiService
import com.example.tesths.domain.model.Product
import com.example.tesths.domain.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryImpl (
    private val apiService: ApiService,
    private val mapper: Mapper,
) : Repository {

    override fun getProducts(skip: Int,limit: Int): Flow<List<Product>> = flow {
        val response = apiService.getProducts(skip,limit)
        val products = response.products.map { mapper.mapProductDtoToEntity(it) }
        emit(products)
    }
}