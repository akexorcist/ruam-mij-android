plugins {
    alias(libs.plugins.androidLibrary)
}

android {
    namespace = "com.akexorcist.ruammij.functional.device"
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
    implementation(project(":base:common"))
    implementation(project(":base:data"))
    implementation(project(":base:utility"))
    coreLibraryDesugaring(libs.desugar)
    implementation(libs.kotlin.coroutines.core)
}
