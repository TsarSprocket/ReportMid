import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.matchUpView.impl",
    enableCompose = true,
) {
    with(projects) {
        api(matchUpView.api)

        impl(appApi)
        impl(baseApi)
        impl(currentGameData.api)
        impl(dataDragonApi)
        impl(utils)
        impl(viewStateApi)

        // KSP
        impl(kspApi)
        ksp(kspProcessor)
    }

    with(libs) {
        // Dagger
        kapt(dagger.compiler)
        kapt(dagger.android.processor)

        // Compose
        impl(platform(compose.bom))
        impl(compose.runtime)
        impl(compose.material3)
        impl(compose.ui.tooling.preview)
    }
}
