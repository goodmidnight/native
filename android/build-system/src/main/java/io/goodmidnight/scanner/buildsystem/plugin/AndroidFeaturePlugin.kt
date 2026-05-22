package io.goodmidnight.scanner.buildsystem.plugin

import io.goodmidnight.scanner.buildsystem.util.Extensions.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("implementation", versionCatalog.findLibrary("androidx-appcompat").get())
                add("implementation", versionCatalog.findLibrary("androidx-activity-ktx").get())
                add(
                    "implementation",
                    versionCatalog.findLibrary("androidx-core-splashscreen").get()
                )
                add("implementation", versionCatalog.findLibrary("androidx-core-ktx").get())
                add(
                    "implementation",
                    versionCatalog.findLibrary("androidx-lifecycle-service").get()
                )
                add(
                    "implementation",
                    versionCatalog.findLibrary("androidx-lifecycle-runtime-ktx").get()
                )
                add(
                    "implementation",
                    versionCatalog.findLibrary("androidx-lifecycle-livedata-ktx").get()
                )
                add(
                    "implementation",
                    versionCatalog.findLibrary("androidx-navigation-runtime-ktx").get()
                )
                add("implementation", versionCatalog.findLibrary("reorderable").get())


                add(
                    "androidTestImplementation",
                    versionCatalog.findLibrary("androidx-navigation-testing").get()
                )
                add(
                    "androidTestImplementation",
                    versionCatalog.findLibrary("androidx-test-rules").get()
                )
            }
        }
    }
}
