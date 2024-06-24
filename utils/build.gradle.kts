import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.utils",
) {
    impl(libs.androidx.core.ktx)
    impl(libs.androidx.appcompat)
    impl(libs.material)
}