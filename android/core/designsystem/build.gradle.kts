
plugins {
    alias(libs.plugins.scanner.android.library)
    alias(libs.plugins.scanner.android.library.compose)
    alias(libs.plugins.scanner.android.compose.ext)
    alias(libs.plugins.scanner.android.image)
    alias(libs.plugins.scanner.android.kotlin)
    alias(libs.plugins.scanner.android.test)
}

android {
    namespace = "io.goodmidnight.scanner.designsystem"
}


