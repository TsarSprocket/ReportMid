import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.appBar.impl",
    enableCompose = true,
) {
    with(projects) {
        api(appBar.api)

        impl(appApi)
        impl(baseApi)
        impl(viewStateApi)

        impl(kspApi)
        ksp(kspProcessor)
    }

    with(libs) {
        kapt(dagger.compiler)
        kapt(dagger.android.processor)

        impl(platform(compose.bom))
        impl(compose.foundation)
        impl(compose.runtime)
        impl(compose.material3)
        impl(compose.ui.tooling.preview)
    }
}
