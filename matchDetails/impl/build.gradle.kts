import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.debug
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.matchDetails.impl",
    enableCompose = true,
) {
    with(projects) {
        api(matchDetails.api)

        impl(appApi)
        impl(dataDragonApi)
        impl(matchData.api)
        impl(navigationMapApi)
        impl(kspApi)
        impl(resLib)
        impl(summonerViewApi)
        impl(theme)
        impl(viewStateApi)

        // KSP
        impl(kspApi)
        ksp(kspProcessor)
    }

    with(libs) {
        impl(androidx.core.ktx)
        impl(kotlinx.collections.immutable)

        // Dagger
        kapt(dagger.compiler)
        kapt(dagger.android.processor)

        // Compose
        impl(platform(compose.bom))

        // Compose Material 3
        impl(compose.material3)

        // Compose preview support
        impl(compose.ui.tooling.preview)
        debug(compose.ui.tooling.main)

        // Optional - Integration with ViewModels
        impl(androidx.lifecycle.viewmodel.compose)

        // Coil
        impl(coil)
    }
}