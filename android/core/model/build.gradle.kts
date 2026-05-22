plugins {
    alias(libs.plugins.scanner.android.library)
    alias(libs.plugins.scanner.android.kotlin)
    alias(libs.plugins.scanner.android.test)
}

android {
    namespace = "io.goodmidnight.scanner.model"
}
