package io.goodmidnight.scanner.domain.usecase.settings

import io.goodmidnight.scanner.domain.repository.SettingsRepository
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke() = repository.getSettings()
}
