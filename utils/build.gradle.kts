import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.utils",
) {
    impl(libs.androidx.core.ktx)
    impl(libs.androidx.appcompat)

    // Material
    impl(libs.material)

    // Dagger
    api(libs.dagger.main)
}