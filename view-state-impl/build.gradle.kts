import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(namespace = "com.tsarsprocket.reportmid.mvi_impl") {
    impl(project(":base-api"))
    impl(project(":app-api"))
    impl(project(":theme"))
    api(project(":view-state-api"))

    impl(project(":ksp-processor"))
    ksp(project(":ksp-processor"))

    impl(libs.androidx.core.ktx)
    impl(libs.kotlin.reflect)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    // Compose
    impl(platform(libs.compose.bom))
    impl(libs.compose.foundation)

    // Compose Material 3
    impl(libs.compose.material3)
}
