import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.library

library(namespace = "com.tsarsprocket.reportmid.summoner_impl") {

    api(project(":summoner-api"))
    api(project(":summoner-room"))
    api(project(":data-dragon-room"))
    api(project(":lol-services-api"))
    api(project(":app-api"))

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    // Retrofit
    impl(libs.retrofit)
    impl(libs.adapter.rxjava2)
    impl(libs.converter.gson)
}

/*
plugins {
    id 'com.android.library'
    id 'org.jetbrains.kotlin.android'
    id 'org.jetbrains.kotlin.kapt'
}

android {
    namespace 'com.tsarsprocket.reportmid.summoner_impl'
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

    api project(path: ':summoner-api')
    api project(path: ':summoner-room')
    api project(path: ':data-dragon-room')
    api project(path: ':lol-services-api')
    api project(path: ':app-api')

    // Dagger
    kapt libs.dagger.compiler
    kapt libs.dagger.android.processor

    // Retrofit
    implementation libs.retrofit
    implementation libs.adapter.rxjava2
    implementation libs.converter.gson
}
*/
