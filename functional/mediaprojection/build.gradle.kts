plugins {
    alias(libs.plugins.androidLibrary)
}

android {
    namespace = "com.akexorcist.ruammij.functional.mediaprojection"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlin {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
        }
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.androidx.lifecycle.viewmodel)
}
