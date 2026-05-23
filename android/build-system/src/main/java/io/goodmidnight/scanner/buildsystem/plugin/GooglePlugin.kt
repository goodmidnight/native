package io.goodmidnight.scanner.buildsystem.plugin

import io.goodmidnight.scanner.buildsystem.util.Extensions.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class GooglePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.android.gms.oss-licenses-plugin")
            }
            dependencies {
                add("implementation", versionCatalog.findLibrary("google-play-services-oss-licenses").get())
            }
        }
    }
}
