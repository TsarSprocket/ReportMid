import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.debug
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.matchHistory.impl",
    enableCompose = true,
) {
    with(projects) {
        api(matchHistory.api)
        api(theme)

        impl(baseApi)
        impl(dataDragonApi)
        impl(resLib)
        impl(summonerApi)
        impl(utils)

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
