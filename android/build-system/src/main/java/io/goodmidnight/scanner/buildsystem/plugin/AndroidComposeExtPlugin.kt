package io.goodmidnight.scanner.buildsystem.plugin

import io.goodmidnight.scanner.buildsystem.util.Extensions.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeExtPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("implementation", versionCatalog.findLibrary("androidx-activity-compose").get())
                add(
                    "implementation",
                    versionCatalog.findLibrary("androidx-lifecycle-runtime-compose").get()
                )
                add(
                    "implementation",
                    versionCatalog.findLibrary("androidx-lifecycle-viewmodel-compose").get()
                )
                add(
                    "implementation",
                    versionCatalog.findLibrary("androidx-navigation-compose").get()
                )
            }
        }
    }
}
