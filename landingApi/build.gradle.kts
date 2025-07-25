import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.landingApi",
) {
    api(projects.baseApi)
    api(projects.viewStateApi)
    api(projects.lol.api)

    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    impl(libs.androidx.core.ktx)
}