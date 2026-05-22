package io.goodmidnight.scanner.buildsystem.plugin

import io.goodmidnight.scanner.buildsystem.util.Extensions.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin-parcelize")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            dependencies {
                add(
                    "implementation",
                    versionCatalog.findLibrary("kotlinx-coroutines").get()
                )
                add("implementation", versionCatalog.findLibrary("kotlinx-datetime").get())
                add("implementation", versionCatalog.findLibrary("coroutines-guava").get())
                add("implementation", versionCatalog.findLibrary("coroutines-play-services").get())
                add(
                    "implementation",
                    versionCatalog.findLibrary("kotlinx-serialization-json").get()
                )
                add("testImplementation", versionCatalog.findLibrary("kotlinx-coroutines-test").get())
                add("androidTestImplementation", versionCatalog.findLibrary("kotlinx-coroutines-test").get())
            }
        }
    }
}
