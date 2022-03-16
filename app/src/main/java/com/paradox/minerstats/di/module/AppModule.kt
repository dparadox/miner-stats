package com.paradox.minerstats.di.module

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.paradox.minerstats.core.adapter.KeyValueDataStore
import com.paradox.minerstats.core.adapter.KeyValueDataStoreImpl
import com.paradox.minerstats.core.config.Constants.MINER_SHARED_PREFERENCES
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(MINER_SHARED_PREFERENCES, MODE_PRIVATE)

    @Provides
    fun providesKeyValueDataStore(sharedPreferences: SharedPreferences): KeyValueDataStore =
        KeyValueDataStoreImpl(sharedPreferences)
}