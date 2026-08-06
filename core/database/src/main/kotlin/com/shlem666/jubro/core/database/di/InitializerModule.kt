package com.shlem666.jubro.core.database.di

import com.shlem666.jubro.core.database.DatabaseInitializer
import com.shlem666.jubro.core.database.PreloadDatabaseInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InitializerModule {

    @Binds
    internal abstract fun bindsDatabaseInitializer(
        initializer: PreloadDatabaseInitializer
    ): DatabaseInitializer
}