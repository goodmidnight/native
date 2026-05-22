package io.goodmidnight.scanner.domain.usecase

import io.goodmidnight.scanner.domain.repository.ScanRepository
import io.goodmidnight.scanner.model.ImageQuality
import io.goodmidnight.scanner.model.ScannerConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateScannerConfigUseCase @Inject constructor(
    private val scanRepository: ScanRepository
) {
    operator fun invoke(imageQuality: ImageQuality) {
        val config = ScannerConfig(targetWidth = imageQuality.targetWidth)
        scanRepository.updateConfig(config)
    }
}
