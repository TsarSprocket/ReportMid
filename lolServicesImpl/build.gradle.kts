import com.tsarsprocket.reportmid.buildlogic.BuildConfigField
import com.tsarsprocket.reportmid.buildlogic.BuildTypes

plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.lolServicesImpl"
    buildConfigFields = listOf(
        BuildConfigField(
            name = "OKHTTP_LOGGING",
            type = "String",
            values = mapOf(
                BuildTypes.DEBUG to "\"BODY\"",
                BuildTypes.RELEASE to "\"NONE\"",
            ),
        ),
    )
}

dependencies {
    api(projects.lolServicesApi)
    api(projects.appApi)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.adapter.rxjava2)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Dagger
    ksp(libs.dagger.compiler)
    ksp(libs.dagger.android.processor)

    // Standard
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}
