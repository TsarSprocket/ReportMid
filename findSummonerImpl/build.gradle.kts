import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.debug
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.findSummonerImpl",
    enableCompose = true,
) {
    api(projects.appApi)
    impl(projects.dataDragonApi)
    api(projects.findSummonerApi)
    impl(projects.lol.api)
    impl(projects.summonerApi)
    impl(projects.theme)
    impl(projects.utils)
    impl(projects.navigationMapApi)

    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    impl(projects.kspApi)
    ksp(projects.kspProcessor)

    impl(libs.androidx.core.ktx)

    // Compose
    impl(platform(libs.compose.bom))

    // Compose Material 3
    impl(libs.compose.material3)

    // Compose preview support
    impl(libs.compose.ui.tooling.preview)
    debug(libs.compose.ui.tooling.main)

    // Coil
    impl(libs.coil)
}
