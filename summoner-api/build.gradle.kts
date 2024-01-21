import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.library

library(namespace = "com.tsarsprocket.reportmid.summoner_api") {

    api(project(":base"))
    api(project(":lol"))

    // Rx
    impl(libs.rxandroid)
}

/*
plugins {
    id 'com.android.library'
    id 'org.jetbrains.kotlin.android'
}

android {
    namespace 'com.tsarsprocket.reportmid.summoner_api'
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

    api project(path: ':base')
    api project(path: ':lol')


    // Rx
    implementation libs.rxandroid
//    implementation libs.rxkotlin
}
*/
