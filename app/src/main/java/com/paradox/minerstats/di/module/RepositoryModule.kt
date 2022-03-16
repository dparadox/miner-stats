package com.paradox.minerstats.di.module

import com.paradox.minerstats.model.repository.Repository
import com.paradox.minerstats.model.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(repository: RepositoryImpl) : Repository
}