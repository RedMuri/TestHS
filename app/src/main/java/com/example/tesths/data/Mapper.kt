package com.example.tesths.data

import com.example.tesths.data.model.ProductDto
import com.example.tesths.domain.model.Product

class Mapper{

    fun mapProductDtoToEntity(productDto: ProductDto) = Product(
        id = productDto.id,
        title = productDto.title,
        price = productDto.price,
        description = productDto.description,
        image = productDto.image
    )
}