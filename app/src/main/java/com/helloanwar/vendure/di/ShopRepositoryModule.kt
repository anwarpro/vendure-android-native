package com.helloanwar.vendure.di

import com.helloanwar.vendure.data.source.ShopRepository
import com.helloanwar.vendure.data.source.remote.ShopRemoteSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ShopRepositoryModule {
    @Provides
    fun provideShopRepository(remoteSource: ShopRemoteSource): ShopRepository {
        return ShopRepository(remoteSource = remoteSource)
    }

    @Provides
    fun provideShopRemoteSource(): ShopRemoteSource {
        return ShopRemoteSource()
    }
}