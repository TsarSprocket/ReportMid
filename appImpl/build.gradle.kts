import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.application
import com.tsarsprocket.reportmid.gradle.debug
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp

application(
    appId = "com.tsarsprocket.reportmid.app",
    namespace = "com.tsarsprocket.reportmid.appImpl",
) {
    api(projects.baseApi)
    api(projects.appApi)
    impl(projects.baseImpl)
    impl(projects.dataDragonImpl)
    impl(projects.landingImpl)
    impl(projects.leaguePositionImpl)
    impl(projects.lolServicesImpl)
    impl(projects.requestManagerImpl)
    impl(projects.stateImpl)
    impl(projects.summonerImpl)
    impl(projects.viewStateImpl)

    impl(projects.kspProcessor)
    ksp(projects.kspProcessor)

    // Android
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

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)
}
