import org.jetbrains.kotlin.gradle.dsl.JvmTarget
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
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_18)
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

gradlePlugin {
    plugins {
        register("reportMidApplication") {
            id = "reportmid.android.application"
            implementationClass = "com.tsarsprocket.reportmid.buildlogic.ReportMidApplicationConventionPlugin"
        }
        register("reportMidLibrary") {
            id = "reportmid.android.library"
            implementationClass = "com.tsarsprocket.reportmid.buildlogic.ReportMidLibraryConventionPlugin"
        }
        register("reportMidLibraryCompose") {
            id = "reportmid.android.library.compose"
            implementationClass = "com.tsarsprocket.reportmid.buildlogic.ReportMidLibraryComposeConventionPlugin"
        }
        register("reportMidJavaLibrary") {
            id = "reportmid.jvm.library"
            implementationClass = "com.tsarsprocket.reportmid.buildlogic.ReportMidJavaLibraryConventionPlugin"
        }
    }
}
