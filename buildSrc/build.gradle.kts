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
    implementation(libs.gradle.android)
    gradleKotlinDsl()
    implementation(libs.kotlin.gradle.plugin)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}