package com.paradox.minerstats.di.module

import android.content.Context
import com.paradox.minerstats.core.adapter.ResourceAdapter
import com.paradox.minerstats.core.adapter.ResourceAdapterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ViewModelComponent::class)
@Module
object AdapterModule {

    @Provides
    fun providesResourceAdapter(@ApplicationContext context: Context): ResourceAdapter =
        ResourceAdapterImpl(context)
}