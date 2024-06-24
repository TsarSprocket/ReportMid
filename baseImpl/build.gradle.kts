import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.baseImpl",
) {
    api(projects.baseApi)
    api(projects.appApi)

    impl(projects.kspProcessor)
    ksp(projects.kspProcessor)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)
}