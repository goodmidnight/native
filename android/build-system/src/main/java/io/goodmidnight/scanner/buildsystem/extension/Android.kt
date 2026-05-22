package io.goodmidnight.scanner.buildsystem.extension

import com.android.build.api.dsl.CommonExtension
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.JAVA_VERSION
import org.gradle.api.Project

internal fun Project.extensionAndroid(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        defaultConfig.apply {
            buildFeatures.buildConfig = true

            compileOptions.apply {
                sourceCompatibility = JAVA_VERSION
                targetCompatibility = JAVA_VERSION
            }



            packaging.apply {
                resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
                resources.excludes.add("/META-INF/gradle/incremental.annotation.processors")
            }
        }
    }
}
