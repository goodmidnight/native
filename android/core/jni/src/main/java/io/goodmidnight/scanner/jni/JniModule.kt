package io.goodmidnight.scanner.jni

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.goodmidnight.scanner.data.datasource.ScannerDataSource
import io.goodmidnight.scanner.model.ScannerConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object JniModule {

    @Provides
    @Singleton
    fun provideNativeScanner(): ScannerDataSource {
        val scanner = NativeScanner()
        scanner.initEngine(ScannerConfig(targetWidth = 800))
        return scanner
    }
}
