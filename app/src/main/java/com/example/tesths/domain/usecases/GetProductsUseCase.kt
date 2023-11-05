package com.example.tesths.domain.usecases

import com.example.tesths.domain.Repository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke(skip: Int, limit: Int) = repository.getProducts(skip, limit)
}
