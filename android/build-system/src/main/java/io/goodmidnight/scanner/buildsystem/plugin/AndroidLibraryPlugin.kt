package io.goodmidnight.scanner.buildsystem.plugin

import com.android.build.api.dsl.LibraryExtension
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.BuildType.DEBUG
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.BuildType.RELEASE
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.COMPILE_SDK
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.Flavor.FLAVOR_LIST
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.MIN_SDK
import io.goodmidnight.scanner.buildsystem.extension.configureFlavor
import io.goodmidnight.scanner.buildsystem.extension.extensionAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                extensionAndroid(this)
                configureFlavor(this, FLAVOR_LIST)

                buildFeatures {
                    resValues = true
                }

                defaultConfig {
                    compileSdk = COMPILE_SDK
                    minSdk = MIN_SDK
                    vectorDrawables.useSupportLibrary = true
                }

                buildTypes {
                    getByName(DEBUG) {
                        isMinifyEnabled = false
                    }
                    getByName(RELEASE) {
                        // 릴리즈 빌드에서 ProGuard를 활성화
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro", "../../proguard-common.pro"
                        )
                    }
                }
            }
        }
    }
}
