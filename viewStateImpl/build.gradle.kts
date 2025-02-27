import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.debug
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.viewStateImpl",
    enableCompose = true,
) {
    impl(projects.baseApi)
    impl(projects.appApi)
    impl(projects.theme)
    api(projects.viewStateApi)

    impl(projects.kspApi)
    ksp(projects.kspProcessor)

    impl(libs.androidx.core.ktx)
    impl(libs.kotlin.reflect)
    impl(libs.kotlin.stdlib)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    // Compose
    impl(platform(libs.compose.bom))
    impl(libs.compose.foundation)

    // Compose Material 3
    impl(libs.compose.material3)

    // Compose preview support
    impl(libs.compose.ui.tooling.preview)
    debug(libs.compose.ui.tooling.main)
}
