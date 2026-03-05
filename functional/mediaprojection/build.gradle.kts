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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        isCoreLibraryDesugaringEnabled = true
    }
    kotlin {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
        }
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.androidx.lifecycle.viewmodel)
}
