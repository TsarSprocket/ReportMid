import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.application
import com.tsarsprocket.reportmid.gradle.debug
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp

application(
    appId = "com.tsarsprocket.reportmid.app",
    namespace = "com.tsarsprocket.reportmid.app_impl",
) {
    api(project(":base-api"))
    api(project(":app-api"))
    impl(project(":base-impl"))
    impl(project(":data-dragon-impl"))
    impl(project(":landing-impl"))
    impl(project(":league-position-impl"))
    impl(project(":lol-services-impl"))
    impl(project(":request-manager-impl"))
    impl(project(":state-impl"))
    impl(project(":summoner-impl"))
    impl(project(":view-state-impl"))

    impl(project(":ksp-processor"))
    ksp(project(":ksp-processor"))

    // Android
    impl(libs.androidx.core.ktx)
    impl(libs.androidx.lifecycle.runtime.ktx)
    impl(libs.androidx.activity.compose)
    impl(platform(libs.compose.bom))
    impl(libs.androidx.ui.main)
    impl(libs.androidx.ui.graphics)
    impl(libs.compose.ui.tooling.preview)
    impl(libs.compose.material3)
    debug(libs.compose.ui.tooling.main)
    debug(libs.androidx.ui.test.manifest)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)
}
