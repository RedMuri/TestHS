package com.example.tesths.data

import com.example.tesths.data.database.ProductDbModel
import com.example.tesths.data.model.ProductDto
import com.example.tesths.domain.model.Product
import javax.inject.Inject

class Mapper @Inject constructor(){

    fun mapProductDtoToDbModel(productDto: ProductDto) = ProductDbModel(
        id = productDto.id,
        title = productDto.title,
        price = productDto.price,
        description = productDto.description,
        image = productDto.image
    )

    fun mapProductDbModelToEntity(productDbModel: ProductDbModel) = Product(
        id = productDbModel.id,
        title = productDbModel.title,
        price = productDbModel.price,
        description = productDbModel.description,
        image = productDbModel.image
    )
}