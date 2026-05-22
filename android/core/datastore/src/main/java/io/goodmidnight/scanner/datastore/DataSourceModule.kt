package io.goodmidnight.scanner.datastore

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.goodmidnight.scanner.data.datasource.SettingsDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    @Singleton
    fun bindSettingsDataSource(
        settingsDataStore: SettingsDataStore,
    ): SettingsDataSource
}