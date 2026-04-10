package io.goodmidnight.scanner.camera

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import com.google.common.util.concurrent.ListenableFuture
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.goodmidnight.scanner.data.jni.NativeScanner
import io.goodmidnight.scanner.domain.model.ScannerConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CameraModule {

    @Provides
    @Singleton
    fun provideProcessCameraProviderFuture(
        @ApplicationContext context: Context
    ): ListenableFuture<ProcessCameraProvider> {
        return ProcessCameraProvider.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideNativeScanner(): NativeScanner {
        val scanner = NativeScanner()
        scanner.initEngine(ScannerConfig(targetWidth = 800))
        return scanner
    }
}