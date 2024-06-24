import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.dataDragonImpl",
) {
    api(projects.appApi)
    api(projects.dataDragonApi)
    api(projects.dataDragonRoom)
    api(projects.lolServicesApi)

    // Rx
    impl(libs.rxandroid)
    impl(libs.rxkotlin)
    impl(libs.kotlinx.coroutines.rx2)

    // Retrofit
    impl(libs.retrofit)
    impl(libs.adapter.rxjava2)
    impl(libs.converter.gson)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    impl(libs.androidx.core.ktx)
    impl(libs.androidx.appcompat)
}
