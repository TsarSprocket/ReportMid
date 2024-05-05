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
    api(project(":request-manager-api"))
    api(project(":utils"))

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
