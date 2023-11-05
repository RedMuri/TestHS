package com.example.tesths.di

import android.app.Application
import com.example.macttestapp.di.ApplicationScope
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
        fun provideApiService(application: Application): ApiService {
            return ApiFactory.apiService
        }
    }
}