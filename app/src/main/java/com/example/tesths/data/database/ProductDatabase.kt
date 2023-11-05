package com.example.tesths.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductDbModel::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {
    companion object {
        private const val DB_NAME = "products.db"
        private var database: ProductDatabase? = null
        private var LOCK = Any()

        fun getInstance(context: Context): ProductDatabase {
            synchronized(LOCK) {
                database?.let { return it }
                val instance = Room.databaseBuilder(context, ProductDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build()
                database = instance
                return instance
            }
        }
    }

    abstract fun getProductDao(): ProductDao
}