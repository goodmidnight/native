package io.goodmidnight.scanner.buildsystem.extension.type

import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.VERSION_CODE
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.VERSION_NAME

data class FlavorType(
    val name: String,
    val dimension: FlavorDimension = FlavorDimension.Version,
    val suffix: String? = null,
    val versionCode: Int = VERSION_CODE,
    val versionName: String = VERSION_NAME,
    val buildConfigField: List<BuildConfigField> = emptyList(),
    val resourceValue: List<ResourceType> = emptyList(),
    val manifestPlaceholder: Map<String, Any> = emptyMap()
) {
    enum class FlavorDimension { Version }
}
