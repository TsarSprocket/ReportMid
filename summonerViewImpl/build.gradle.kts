import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.debug
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.summonerViewImpl",
    enableCompose = true,
) {
    with(projects) {
        api(summonerViewApi)

        impl(navigationMapApi)
        impl(profileOverviewApi)
        impl(resLib)
        impl(matchHistory.api)
        impl(theme)

        impl(kspApi)
        ksp(kspProcessor)
    }

    with(libs) {
        impl(androidx.core.ktx)

        kapt(dagger.compiler)
        kapt(dagger.android.processor)

        // Compose
        impl(platform(compose.bom))

        // Compose Material 3
        impl(compose.material3)
        impl(compose.material.icons.core)

        // Compose preview support
        impl(compose.ui.tooling.preview)
        debug(compose.ui.tooling.main)

        // Optional - Integration with ViewModels
        impl(androidx.lifecycle.viewmodel.compose)
    }
}