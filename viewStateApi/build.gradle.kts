import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.debug
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.viewStateApi",
    enableCompose = true,
) {
    api(projects.baseApi)

    impl(libs.androidx.core.ktx)

    // Compose
    api(platform(libs.compose.bom))
    api(libs.compose.foundation)

    // Compose Material 3
    api(libs.compose.material3)

    // Compose preview support
    impl(libs.compose.ui.tooling.preview)
    debug(libs.compose.ui.tooling.main)
}