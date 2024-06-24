import com.tsarsprocket.reportmid.gradle.debug
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.viewStateApi",
    enableCompose = true,
) {
    impl(projects.baseApi)
    impl(libs.androidx.core.ktx)

    // Compose
    impl(platform(libs.compose.bom))
    impl(libs.compose.foundation)

    // Compose preview support
    impl(libs.compose.ui.tooling.preview)
    debug(libs.compose.ui.tooling.main)

    impl(libs.androidx.core.ktx)
}