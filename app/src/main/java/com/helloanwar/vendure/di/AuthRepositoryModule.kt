package com.helloanwar.vendure.di

import com.helloanwar.vendure.data.source.AuthRepository
import com.helloanwar.vendure.data.source.remote.AuthRemoteSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {
    @Provides
    fun provideAuthRepository(
        remoteSource: AuthRemoteSource
    ): AuthRepository {
        return AuthRepository(remoteSource = remoteSource)
    }

    @Provides
    fun provideRemoteSource(): AuthRemoteSource {
        return AuthRemoteSource()
    }
}