import com.tsarsprocket.reportmid.gradle.application
import com.tsarsprocket.reportmid.gradle.debug
import com.tsarsprocket.reportmid.gradle.impl

application(
    appId = "com.tsarsprocket.reportmid.app",
    namespace = "com.tsarsprocket.reportmid.app_impl",
) {
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
}
