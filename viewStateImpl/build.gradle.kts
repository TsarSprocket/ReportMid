import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(namespace = "com.tsarsprocket.reportmid.viewStateImpl") {
    impl(projects.baseApi)
    impl(projects.appApi)
    impl(projects.theme)
    api(projects.viewStateApi)

    impl(projects.kspProcessor)
    ksp(projects.kspProcessor)

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
