import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.library
import com.tsarsprocket.reportmid.gradle.test

library(namespace = "com.tsarsprocket.reportmid.summoner_room") {

    impl(project(":lol-room"))

    // Room
    impl(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    impl(libs.androidx.room.ktx)
    impl(libs.androidx.room.rxjava2)
    test(libs.androidx.room.testing)
}

/*
plugins {
    id 'com.android.library'
    id 'org.jetbrains.kotlin.android'
    id 'org.jetbrains.kotlin.kapt'
}

android {
    namespace 'com.tsarsprocket.reportmid.summoner_room'
    compileSdk compile_sdk_version

    defaultConfig {
        minSdk min_sdk_version
        targetSdk target_sdk_version

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles "consumer-rules.pro"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_18
        targetCompatibility JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_18.toString()
    }
}

dependencies {

    implementation project(path: ':lol-room')

    // Room
    implementation libs.androidx.room.runtime
    kapt libs.androidx.room.compiler
    implementation libs.androidx.room.ktx
    implementation libs.androidx.room.rxjava2
    testImplementation libs.androidx.room.testing
}
*/
