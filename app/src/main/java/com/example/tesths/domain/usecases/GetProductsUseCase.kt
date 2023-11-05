package com.example.tesths.domain.usecases

import com.example.tesths.domain.Repository

class GetProductsUseCase(private val repository: Repository) {
    operator fun invoke(skip: Int, limit: Int) = repository.getProducts(skip, limit)
}
