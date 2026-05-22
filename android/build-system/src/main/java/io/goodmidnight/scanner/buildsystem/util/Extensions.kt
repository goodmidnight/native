package io.goodmidnight.scanner.buildsystem.util

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import java.util.Properties

internal object Extensions {

    val Project.applicationExtension: ApplicationExtension
        get(): ApplicationExtension = extensions.getByType<ApplicationExtension>()

    val Project.libraryExtension: LibraryExtension
        get(): LibraryExtension = extensions.getByType<LibraryExtension>()

    val Project.versionCatalog: VersionCatalog
        get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

    val Project.localProperties
        get(): Properties = Properties().apply {
            val localPropertiesFile = rootProject.file("local.properties")
            if (localPropertiesFile.exists()) {
                load(localPropertiesFile.inputStream())
            }
        }
}
