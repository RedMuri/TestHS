package com.example.tesths.di

import com.example.tesths.data.RepositoryImpl
import com.example.tesths.data.network.ApiFactory
import com.example.tesths.data.network.ApiService
import com.example.tesths.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: RepositoryImpl): Repository

    companion object {

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}