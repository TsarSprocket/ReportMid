import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.matchData.impl",
) {
    with(projects) {
        api(matchData.api)

        impl(baseApi)
        impl(dataDragonApi)
        impl(lolServicesApi)
        impl(utils)

        // KSP
        impl(kspApi)
        ksp(kspProcessor)
    }

    with(libs) {
        impl(androidx.core.ktx)

        // Dagger
        kapt(dagger.compiler)
        kapt(dagger.android.processor)

        // Retrofit
        impl(libs.retrofit)
        impl(libs.converter.gson)
    }
}
