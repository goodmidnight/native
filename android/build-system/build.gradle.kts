import org.gradle.kotlin.dsl.compileOnly

plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.android.gradle)
    compileOnly(libs.compose.gradle)
    compileOnly(libs.ksp.gradle)
}

gradlePlugin {
    plugins {
        register("scanner.android.application") {
            id = "scanner.android.application"
            implementationClass = "io.goodmidnight.scanner.buildsystem.plugin.AndroidApplicationPlugin"
        }
        register("scanner.android.library") {
            id = "scanner.android.library"
            implementationClass = "io.goodmidnight.scanner.buildsystem.plugin.AndroidLibraryPlugin"
        }
        register("scanner.android.application.compose") {
            id = "scanner.android.application.compose"
            implementationClass = "io.goodmidnight.scanner.buildsystem.plugin.AndroidApplicationComposePlugin"
        }
        register("scanner.android.library.compose") {
            id = "scanner.android.library.compose"
            implementationClass = "io.goodmidnight.scanner.buildsystem.plugin.AndroidLibraryComposePlugin"
        }
        register("scanner.android.camera") {
            id = "scanner.android.camera"
            implementationClass = "io.goodmidnight.scanner.buildsystem.plugin.CameraPlugin"
        }
        register("scanner.android.compose.ext") {
            id = "scanner.android.compose.ext"
            implementationClass = "io.goodmidnight.scanner.buildsystem.plugin.AndroidComposeExtPlugin"
        }
        register("scanner.android.feature") {
            id = "scanner.android.feature"
            implementationClass = "io.goodmidnight.scanner.buildsystem.plugin.AndroidFeaturePlugin"
        }
        register("scanner.android.datastore") {
            id = "scanner.android.datastore"
            implementationClass = "io.goodmidnight.scanner.buildsystem.plugin.DataStorePlugin"
        }
        register("scanner.android.google") {
            id = "scanner.android.google"
            implementationClass = "io.goodmidnight.scanner.buildsystem.plugin.GooglePlugin"
        }
        register("scanner.android.hilt") {
            id = "scanner.android.hilt"
            implementationClass = "io.goodmidnight.scanner.buildsystem.plugin.HiltPlugin"
        }
        register("scanner.android.image") {
            id = "scanner.android.image"
            implementationClass = "io.goodmidnight.scanner.buildsystem.plugin.ImagePlugin"
        }
        register("scanner.android.kotlin") {
            id = "scanner.android.kotlin"
            implementationClass = "io.goodmidnight.scanner.buildsystem.plugin.KotlinPlugin"
        }
        register("scanner.android.ml") {
            id = "scanner.android.ml"
            implementationClass = "io.goodmidnight.scanner.buildsystem.plugin.MlPlugin"
        }
        register("scanner.android.room") {
            id = "scanner.android.room"
            implementationClass = "io.goodmidnight.scanner.buildsystem.plugin.RoomPlugin"
        }
        register("scanner.android.test") {
            id = "scanner.android.test"
            implementationClass = "io.goodmidnight.scanner.buildsystem.plugin.TestPlugin"
        }
    }
}
