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
    with(projects) {
        api(appApi)

        impl(baseImpl)
        impl(dataDragonImpl)
        impl(findSummonerImpl)
        impl(landingImpl)
        impl(leaguePositionImpl)
        impl(lolServicesImpl)
        impl(mainScreenImpl)
        impl(navigationMapImpl)
        impl(profileOverviewImpl)
        impl(requestManagerImpl)
        impl(stateImpl)
        impl(summonerImpl)
        impl(summonerViewImpl)
        impl(viewStateImpl)

        impl(kspApi)
        ksp(kspProcessor)
    }

    with(libs) {
        // Android
        impl(androidx.core.ktx)
        impl(androidx.lifecycle.runtime.ktx)
        impl(androidx.activity.compose)
        impl(platform(compose.bom))
        impl(androidx.ui.main)
        impl(androidx.ui.graphics)
        impl(compose.ui.tooling.preview)
        impl(compose.material3)
        debug(compose.ui.tooling.main)
        debug(androidx.ui.test.manifest)

        // Dagger
        kapt(dagger.compiler)
        kapt(dagger.android.processor)

        // Room
        api(androidx.room.runtime)
        kapt(androidx.room.compiler)
        api(androidx.room.ktx)
    }
}
