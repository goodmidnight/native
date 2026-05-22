package io.goodmidnight.scanner.buildsystem.plugin

import com.android.build.api.dsl.ApplicationExtension
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.APPLICATION_ID
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.BuildType.DEBUG
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.BuildType.RELEASE
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.COMPILE_SDK
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.Flavor.FLAVOR_LIST
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.MIN_SDK
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.TARGET_SDK
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.VERSION_CODE
import io.goodmidnight.scanner.buildsystem.AndroidBuildConfig.VERSION_NAME
import io.goodmidnight.scanner.buildsystem.extension.configureFlavor
import io.goodmidnight.scanner.buildsystem.extension.extensionAndroid
import io.goodmidnight.scanner.buildsystem.util.Extensions.localProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.File
import kotlin.collections.addAll

class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension> {
                extensionAndroid(this)
                configureFlavor(this, FLAVOR_LIST)

                signingConfigs {
                    create(RELEASE) {
                        storeFile = file("./scanner.jks")
                        storePassword =  localProperties["STORE_PASSWORD"] as String
                        keyAlias = localProperties["KEY_ALIAS"] as String
                        keyPassword = localProperties["KEY_PASSWORD"] as String
                    }
                }

                defaultConfig {
                    applicationId = APPLICATION_ID
                    minSdk = MIN_SDK
                    targetSdk = TARGET_SDK
                    compileSdk = COMPILE_SDK
                    versionCode = VERSION_CODE
                    versionName = VERSION_NAME
                    vectorDrawables.useSupportLibrary = true

                    buildTypes {
                        getByName(DEBUG) {
                            isDebuggable = true
                            isMinifyEnabled = false
                            isShrinkResources = false
                        }
                        getByName(RELEASE) {
                            signingConfig = signingConfigs.getByName(RELEASE)
                            isMinifyEnabled = true
                            isShrinkResources = true
                            proguardFiles(
                                getDefaultProguardFile("proguard-android-optimize.txt"),
                                "../../proguard-common.pro", "proguard-rules.pro"
                            )
                        }
                    }
                }
            }
        }
    }
}
