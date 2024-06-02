import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.library

library(namespace = "com.tsarsprocket.reportmid.summonerImpl") {

    api(projects.summonerApi)
    api(projects.summonerRoom)
    api(projects.dataDragonRoom)
    api(projects.lolServicesApi)
    api(projects.appApi)
    api(projects.requestManagerApi)
    api(projects.utils)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    // Retrofit
    impl(libs.retrofit)
    impl(libs.adapter.rxjava2)
    impl(libs.converter.gson)

    // Reactive streams
    impl(libs.reactivestreams)
}
