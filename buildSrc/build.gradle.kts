import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
}

plugins {
    `kotlin-dsl`
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "${JavaVersion.VERSION_18}"
    }
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "${JavaVersion.VERSION_18}"
    targetCompatibility = "${JavaVersion.VERSION_18}"
}

dependencies {
    gradleApi()
    implementation("com.android.tools.build:gradle:8.5.1")
    gradleKotlinDsl()
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")
}