import com.tsarsprocket.reportmid.gradle.ConfigVersions.COMPILE_SDK_VERSION
import com.tsarsprocket.reportmid.gradle.ConfigVersions.COMPOSE_COMPILER_VERSION
import com.tsarsprocket.reportmid.gradle.ConfigVersions.MIN_SDK_VERSION
import com.tsarsprocket.reportmid.gradle.ConfigVersions.TARGET_SDK_VERSION

plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.jetbrains.kotlin.android.get().pluginId)
    id(libs.plugins.compose.compiler.get().pluginId)
    id(libs.plugins.jetbrains.kotlin.kapt.get().pluginId)
    id(libs.plugins.navigation.safeargs.get().pluginId)
    id(libs.plugins.devtools.ksp.get().pluginId)
}

android {


    compileSdk = COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = "com.tsarsprocket.reportmid"
        minSdk = MIN_SDK_VERSION
        targetSdk = TARGET_SDK_VERSION
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_18.toString()
    }

    composeOptions {
        kotlinCompilerExtensionVersion = COMPOSE_COMPILER_VERSION
    }

    namespace = "com.tsarsprocket.reportmid"
}

repositories {
    mavenCentral() // for Orianna API
}

dependencies {

    implementation(libs.androidx.multidex)
    implementation(libs.android.support.multidex)

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":baseImpl"))
//    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"

    implementation(project(":appApi"))
    implementation(project(":viewStateImpl"))
    implementation(project(":lolResourceLib"))
    implementation(project(":landingImpl"))
    implementation(project(":lol"))
    implementation(project(":lolRoom"))
    implementation(project(":baseApi"))
    implementation(project(":lolServicesImpl"))
    implementation(project(":theme"))
    implementation(project(":leaguePositionImpl"))
    implementation(project(":dataDragonImpl"))
    implementation(project(":summonerImpl"))
    implementation(project(":stateImpl"))
    implementation(project(":requestManagerImpl"))
    implementation(project(":kspProcessor"))
    ksp(project(":kspProcessor"))


    // Dagger
    implementation(libs.dagger.main)
    implementation(libs.dagger.android)
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    // AndroidX
    implementation(libs.reactivestreams)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.drawerlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.preference.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Room
    implementation(libs.androidx.room.rxjava2)
    kapt(libs.androidx.room.compiler)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Orianna API
    implementation("com.merakianalytics.orianna:orianna-android:4.0.0-rc6") // Will be sun set after the migration to the RIOT API

    // Rx
    implementation(libs.rxandroid)
    implementation(libs.rxkotlin)

    // Material
    implementation(libs.material)

    // GSON
    implementation(libs.gson)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.adapter.rxjava2)
    implementation(libs.converter.gson)

    // Paging 3
    implementation(libs.androidx.paging.runtime.ktx)
    testImplementation(libs.androidx.paging.common.ktx) // alternatively - without Android dependencies for tests
    implementation(libs.androidx.paging.rxjava2.ktx) // optional - RxJava2 support

    // Channels
    implementation(libs.kotlinx.coroutines.core)

    // Flow
    implementation(libs.kotlinx.coroutines.rx2)

    // Compose
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Compose Material 3
    implementation(libs.compose.material3)

    // Compose preview support
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling.main)

    // Optional - Integration with activities
    // implementation "androidx.activity:activity-compose:1.7.0"
    // Optional - Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Optional - Integration with RxJava
    // implementation 'androidx.compose.runtime:runtime-rxjava2'
}
