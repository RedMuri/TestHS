package com.example.tesths.di

import androidx.lifecycle.ViewModel
import com.example.tesths.ui.viewmodel.ProductsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProductsViewModel::class)
    fun bindProductsViewModel(viewModel: ProductsViewModel): ViewModel
}