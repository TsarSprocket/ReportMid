import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.debug
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.profileScreenImpl",
    enableCompose = true,
) {
    api(projects.profileScreenApi)
    api(projects.theme)

    impl(projects.baseApi)

    impl(libs.androidx.core.ktx)

    // KSP
    impl(projects.kspProcessor)
    ksp(projects.kspProcessor)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    // Compose
    impl(platform(libs.compose.bom))

    // Compose Material 3
    impl(libs.compose.material3)

    // Compose preview support
    impl(libs.compose.ui.tooling.preview)
    debug(libs.compose.ui.tooling.main)

    // Optional - Integration with ViewModels
    impl(libs.androidx.lifecycle.viewmodel.compose)
}