plugins {
    alias(libs.plugins.scanner.android.library)
    alias(libs.plugins.scanner.android.hilt)
    alias(libs.plugins.scanner.android.kotlin)
    alias(libs.plugins.scanner.android.test)
    alias(libs.plugins.scanner.android.datastore)
}

android {
    namespace = "io.goodmidnight.scanner.datastore"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:model"))
}