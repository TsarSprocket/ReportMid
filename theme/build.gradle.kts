import com.tsarsprocket.reportmid.gradle.debug
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.theme",
    enableCompose = true,
) {
    impl(projects.resLib)
    impl(libs.androidx.core.ktx)
    impl(libs.androidx.appcompat)
    impl(libs.material)

    // Compose
    impl(platform(libs.compose.bom))

    // Compose Material 3
    impl(libs.compose.material3)

    // Compose preview support
    impl(libs.compose.ui.tooling.preview)
    debug(libs.compose.ui.tooling.main)
}