package com.helloanwar.vendure.di

import android.content.Context
import com.helloanwar.vendure.data.source.CartRepository
import com.helloanwar.vendure.data.source.remote.CartRemoteSource
import com.helloanwar.vendure.util.SharedPrefUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CartRepositoryModule {
    @Provides
    fun provideCartRepository(remoteSource: CartRemoteSource): CartRepository {
        return CartRepository(remoteSource = remoteSource)
    }

    @Provides
    fun provideShopRemoteSource(): CartRemoteSource {
        return CartRemoteSource()
    }

    @Provides
    fun provideDataStoreUtil(@ApplicationContext context: Context): SharedPrefUtil {
        return SharedPrefUtil(context = context)
    }
}