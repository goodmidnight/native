package io.goodmidnight.scanner.buildsystem.extension

import com.android.build.api.dsl.CommonExtension
import io.goodmidnight.scanner.buildsystem.util.Extensions.versionCatalog
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.extensionAndroidCompose(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        buildFeatures.apply {
            compose = true
            resValues = true
        }

        dependencies {
            val bom = versionCatalog.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("implementation", versionCatalog.findLibrary("androidx-compose-animation").get())
            add("implementation", versionCatalog.findLibrary("androidx-compose-foundation").get())
            add(
                "implementation",
                versionCatalog.findLibrary("androidx-compose-foundation-layout").get()
            )
            add("implementation", versionCatalog.findLibrary("androidx-compose-material").get())
            add(
                "implementation",
                versionCatalog.findLibrary("androidx-compose-material-icons-core").get()
            )
            add(
                "implementation",
                versionCatalog.findLibrary("androidx-compose-material-icons-extended").get()
            )
            add("implementation", versionCatalog.findLibrary("androidx-compose-material3").get())
            add(
                "implementation",
                versionCatalog.findLibrary("androidx-compose-material3-windowsizeclass").get()
            )
            add("implementation", versionCatalog.findLibrary("androidx-compose-runtime").get())
            add("implementation", versionCatalog.findLibrary("androidx-compose-ui").get())
            add("implementation", versionCatalog.findLibrary("androidx-compose-ui-graphics").get())
            add("testImplementation", versionCatalog.findLibrary("androidx-compose-ui-test").get())
            add(
                "implementation",
                versionCatalog.findLibrary("androidx-compose-ui-test-manifest").get()
            )
            add("implementation", versionCatalog.findLibrary("androidx-compose-ui-tooling").get())
            add(
                "implementation",
                versionCatalog.findLibrary("androidx-compose-ui-tooling-preview").get()
            )
        }
    }
}
