import com.tsarsprocket.reportmid.gradle.ConfigVersions.COMPILE_SDK_VERSION
import com.tsarsprocket.reportmid.gradle.ConfigVersions.MIN_SDK_VERSION
import com.tsarsprocket.reportmid.gradle.ConfigVersions.TARGET_SDK_VERSION

// Migrated to KTS for the extension functions validation

plugins {
    id("org.jetbrains.kotlin.kapt")
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tsarsprocket.reportmid.baseApi"
    compileSdk = COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = MIN_SDK_VERSION
        lint.targetSdk = TARGET_SDK_VERSION
        testOptions.targetSdk = TARGET_SDK_VERSION

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
    api(projects.utils)

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