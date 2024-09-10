plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.devToolsKsp)
    alias(libs.plugins.ossLicenses)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.akexorcist.ruammij"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.akexorcist.ruammij"
        minSdk = 23
        targetSdk = 34
        versionCode = 15
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.InstrumentationTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            val signingKeyFile: String? = System.getenv("SIGNING_KEY_STORE_PATH")
            if (signingKeyFile != null) {
                signingConfig = signingConfigs.create("release") {
                    storeFile = file("$signingKeyFile")
                    storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                    keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                    keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
                }
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.useJUnit {
                    if (project.hasProperty("snapshot")) {
                        includeCategories("com.akexorcist.ruammij.utils.SnapshotTests")
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(libs.accompanist.drawable.painter)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)
    implementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.koin.bom))

    testImplementation(libs.androidx.espresso.core)
    testImplementation(libs.androidx.ui.test.junit4)

    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.roborazzi.rule)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.androidx.ui.tooling)

    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

    coreLibraryDesugaring(libs.desugar)
}
