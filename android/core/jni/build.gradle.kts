plugins {
    alias(libs.plugins.scanner.android.library)
    alias(libs.plugins.scanner.android.hilt)
    alias(libs.plugins.scanner.android.kotlin)
    alias(libs.plugins.scanner.android.test)
}

android {
    namespace = "io.goodmidnight.scanner.jni"

    defaultConfig {
        minSdk = 27
        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86_64"))
        }

        externalNativeBuild {
            cmake {
                cppFlags("-std=c++17")
                val openCvPath = File(
                    rootProject.projectDir,
                    "../libs/opencv/android/sdk/native/jni"
                ).absolutePath
                arguments("-DOpenCV_DIR=$openCvPath")
            }
        }
    }
    externalNativeBuild {
        cmake {
            path = file("../../../cpp/android/CMakeLists.txt")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
}
