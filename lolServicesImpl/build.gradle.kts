import com.tsarsprocket.reportmid.gradle.BuildConfigField
import com.tsarsprocket.reportmid.gradle.BuildTypes
import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.lolServicesImpl",
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
) {
    api(projects.lolServicesApi)
    api(projects.appApi)

    // Retrofit
    impl(libs.retrofit)
    impl(libs.adapter.rxjava2)
    impl(libs.converter.gson)
    impl(libs.logging.interceptor)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    // Standard
    impl(libs.androidx.core.ktx)
    impl(libs.androidx.appcompat)
    impl(libs.material)
}