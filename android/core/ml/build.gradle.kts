plugins {
    alias(libs.plugins.scanner.android.library)
    alias(libs.plugins.scanner.android.hilt)
    alias(libs.plugins.scanner.android.kotlin)
    alias(libs.plugins.scanner.android.test)
    alias(libs.plugins.scanner.android.ml)
}

android {
    namespace = "io.goodmidnight.scanner.ml"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
}
