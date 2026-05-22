package io.goodmidnight.scanner.buildsystem.plugin

import io.goodmidnight.scanner.buildsystem.util.Extensions.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class TestPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("testImplementation", versionCatalog.findLibrary("junit").get())
                add("androidTestImplementation", versionCatalog.findLibrary("junit").get())
                add("testImplementation", versionCatalog.findLibrary("truth").get())
                add("androidTestImplementation", versionCatalog.findLibrary("truth").get())
            }
        }
    }
}
