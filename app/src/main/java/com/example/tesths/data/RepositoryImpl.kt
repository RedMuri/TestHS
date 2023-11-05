package com.example.tesths.data

import com.example.tesths.data.database.ProductDao
import com.example.tesths.data.network.ApiService
import com.example.tesths.domain.Repository
import com.example.tesths.ui.state.ProductsScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val productDao: ProductDao,
    private val mapper: Mapper,
) : Repository {

    override fun getProducts(): Flow<ProductsScreenState> = flow {
        try {
            val response = apiService.getProducts()
            val productsDto = response.products
            val productsDbModel = productsDto.map { mapper.mapProductDtoToDbModel(it) }
            productDao.deleteAllProducts()
            productDao.insertProducts(productsDbModel)
            val products = productDao.getAllProducts().map { mapper.mapProductDbModelToEntity(it) }
            emit(ProductsScreenState.Content(products))
        } catch (e: Exception) {
            if (e is UnknownHostException) {
                val products =
                    productDao.getAllProducts().map { mapper.mapProductDbModelToEntity(it) }
                emit(ProductsScreenState.InternetError(products))
            } else
                emit(ProductsScreenState.Error(e))
        }

    }
}