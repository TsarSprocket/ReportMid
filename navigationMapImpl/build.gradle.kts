import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.navigationMapImpl",
) {
    with(projects) {
        api(navigationMapApi)

        impl(baseApi)
        impl(profileOverviewApi)

        impl(kspApi)
        ksp(kspProcessor)
    }

    with(libs) {
        kapt(dagger.compiler)
        kapt(dagger.android.processor)
    }
}