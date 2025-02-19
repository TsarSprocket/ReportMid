import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.navigationMapImpl",
) {
    api(projects.navigationMapApi)

    impl(projects.baseApi)
    impl(projects.profileScreenApi)
    impl(projects.findSummonerApi)

    impl(projects.kspProcessor)
    ksp(projects.kspProcessor)

    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)
}