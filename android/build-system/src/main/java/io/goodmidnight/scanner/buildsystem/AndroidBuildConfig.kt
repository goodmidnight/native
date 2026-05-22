package io.goodmidnight.scanner.buildsystem

import io.goodmidnight.scanner.buildsystem.extension.type.FlavorType
import io.goodmidnight.scanner.buildsystem.extension.type.ResourceType
import org.gradle.api.JavaVersion
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object AndroidBuildConfig {
    const val COMPILE_SDK = 36
    const val TARGET_SDK = 36
    const val MIN_SDK = 28
    const val VERSION_NAME = "0.0.1"
    val VERSION_CODE: Int =
        ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHH")).toInt()

    const val APPLICATION_ID = "io.goodmidnight.scanner"
    val JAVA_VERSION = JavaVersion.VERSION_21
    internal object BuildType {
        const val DEBUG = "debug"
        const val RELEASE = "release"
    }

    internal object Flavor {
        val FLAVOR_LIST: List<FlavorType>
            get() = listOf(
                FlavorType(
                    name = "dev", resourceValue = listOf(
                        ResourceType(
                            type = ResourceType.Type.StringType,
                            name = "app_label",
                            value = "Scanner_dev"
                        )
                    )
                ), FlavorType(
                    name = "staging", resourceValue = listOf(
                        ResourceType(
                            type = ResourceType.Type.StringType,
                            name = "app_label",
                            value = "Scanner_staging"
                        )
                    )
                ), FlavorType(
                    name = "live", resourceValue = listOf(
                        ResourceType(
                            type = ResourceType.Type.StringType,
                            name = "app_label",
                            value = "Scanner"
                        )
                    )
                )
            )
    }
}