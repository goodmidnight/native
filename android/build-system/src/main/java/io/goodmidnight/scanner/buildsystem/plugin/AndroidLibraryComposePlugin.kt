package io.goodmidnight.scanner.buildsystem.plugin

import com.android.build.api.dsl.LibraryExtension
import io.goodmidnight.scanner.buildsystem.extension.extensionAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<LibraryExtension> {
                extensionAndroidCompose(this)
            }
        }
    }
}
