package com.example.tesths.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductDbModel(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val image: String
)
