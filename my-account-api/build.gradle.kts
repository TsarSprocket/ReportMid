import com.tsarsprocket.reportmid.gradle.library

library(namespace = "com.tsarsprocket.reportmid.my_account_api") {
    // no dependencies as far
}

/*
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tsarsprocket.reportmid.my_account_api"
    compileSdk = ConfigVersions.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = ConfigVersions.MIN_SDK_VERSION
        lint.targetSdk = ConfigVersions.TARGET_SDK_VERSION

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

    kotlinOptions.jvmTarget = JavaVersion.VERSION_18.toString()

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_18.toString()
    }
}
*/
