plugins {
    alias(libs.plugins.scanner.android.application)
    alias(libs.plugins.scanner.android.application.compose)
    alias(libs.plugins.scanner.android.compose.ext)
    alias(libs.plugins.scanner.android.camera)
    alias(libs.plugins.scanner.android.datastore)
    alias(libs.plugins.scanner.android.feature)
    alias(libs.plugins.scanner.android.google)
    alias(libs.plugins.scanner.android.hilt)
    alias(libs.plugins.scanner.android.image)
    alias(libs.plugins.scanner.android.kotlin)
    alias(libs.plugins.scanner.android.ml)
    alias(libs.plugins.scanner.android.room)
    alias(libs.plugins.scanner.android.test)
}

android {
    namespace = "io.goodmidnight.scanner"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:ml"))
    implementation(project(":core:jni"))
}

