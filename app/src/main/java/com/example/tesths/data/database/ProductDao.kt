package com.example.tesths.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): List<ProductDbModel>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(coinDbModels: List<ProductDbModel>)
    @Query("DELETE FROM products")
    fun deleteAllProducts()
}