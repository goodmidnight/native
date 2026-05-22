package io.goodmidnight.scanner.ml

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.goodmidnight.scanner.data.datasource.OcrDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    @Singleton
    fun bindOcrDataSource(
        ocrDataSource: MlKitOcrDataSource,
    ): OcrDataSource

}
