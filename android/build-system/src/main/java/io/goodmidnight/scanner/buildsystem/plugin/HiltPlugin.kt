package io.goodmidnight.scanner.buildsystem.plugin

import io.goodmidnight.scanner.buildsystem.util.Extensions.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.dagger.hilt.android")
                apply("com.google.devtools.ksp")
            }
            dependencies {
                add("ksp", versionCatalog.findLibrary("hilt-compiler").get())
                add("implementation", versionCatalog.findLibrary("hilt-android").get())
                add("implementation", versionCatalog.findLibrary("hilt-android-testing").get())
                add("implementation", versionCatalog.findLibrary("androidx-hilt-navigation-compose").get())
            }
        }
    }
}
