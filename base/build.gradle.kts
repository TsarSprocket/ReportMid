// Migrated to KTS for the extension functions validation

plugins {
    id("org.jetbrains.kotlin.kapt")
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tsarsprocket.reportmid.base"
    compileSdk = 34 // compile_sdk_version

    defaultConfig {
        minSdk = 24 // min_sdk_version
        targetSdk = 34 // target_sdk_version

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_18.toString()
    }
}

dependencies {
    api(project(":utils"))

    implementation(libs.listenablefuture)

    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)
    api(libs.androidx.fragment.ktx)

    // Dagger
    api(libs.dagger)
    api(libs.dagger.android)
    api(libs.dagger.android.support)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}