import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.leaguePositionImpl",
) {
    api(projects.leaguePositionApi)
    api(projects.lolServicesApi)
    impl(projects.utils)

    impl(projects.kspProcessor)
    ksp(projects.kspProcessor)

    // Retrofit
    impl(libs.retrofit)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)
}