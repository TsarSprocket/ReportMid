import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.debug
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.landingImpl",
    enableCompose = true,
) {
    api(projects.landingApi)
    impl(projects.baseApi)
    impl(projects.appApi)
    impl(projects.lol)
    impl(projects.resLib)
    api(projects.theme)

    impl(projects.kspProcessor)
    ksp(projects.kspProcessor)

    impl(libs.androidx.core.ktx)

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