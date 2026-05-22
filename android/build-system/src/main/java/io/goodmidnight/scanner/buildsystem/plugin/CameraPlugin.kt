package io.goodmidnight.scanner.buildsystem.plugin

import io.goodmidnight.scanner.buildsystem.util.Extensions.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CameraPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("implementation", versionCatalog.findLibrary("androidx-camera-core").get())
                add("implementation", versionCatalog.findLibrary("androidx-camera-camera2").get())
                add("implementation", versionCatalog.findLibrary("androidx-camera-lifecycle").get())
                add("implementation", versionCatalog.findLibrary("androidx-camera-view").get())
            }
        }
    }
}
