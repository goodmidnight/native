package io.goodmidnight.scanner.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.goodmidnight.scanner.data.repository.DefaultOcrRepository
import io.goodmidnight.scanner.data.repository.DefaultScanRepository
import io.goodmidnight.scanner.data.repository.DefaultSettingsRepository
import io.goodmidnight.scanner.domain.repository.OcrRepository
import io.goodmidnight.scanner.domain.repository.ScanRepository
import io.goodmidnight.scanner.domain.repository.SettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindScanRepository(
        scanRepository: DefaultScanRepository,
    ): ScanRepository

    @Binds
    @Singleton
    fun bindOcrRepository(
        ocrRepository: DefaultOcrRepository,
    ): OcrRepository

    @Binds
    @Singleton
    fun bindSettingsRepository(
        defaultSettingsRepository: DefaultSettingsRepository,
    ): SettingsRepository

}
