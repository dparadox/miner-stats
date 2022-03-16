package com.paradox.minerstats.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Default

@InstallIn(SingletonComponent::class)
@Module
class DispatchersModule {

    @Provides
    fun providesDefaultCoroutineDispatcher(): CoroutineDispatcher = Default
}