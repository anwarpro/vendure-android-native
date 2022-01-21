package com.helloanwar.vendure.di

import com.helloanwar.vendure.data.source.ShopRepository
import com.helloanwar.vendure.ui.home.ProductPagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object PagingModule {
    @Provides
    fun provideProductPagingSource(shopRepository: ShopRepository): ProductPagingSource {
        return ProductPagingSource(shopRepository = shopRepository)
    }
}