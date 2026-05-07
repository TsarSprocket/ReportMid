import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.currentGameData.impl",
) {
    with(projects) {
        api(currentGameData.api)

        impl(dataDragonApi)
        impl(lol.api)
        impl(lolServicesApi)
        impl(requestManagerApi)

        // KSP
        impl(kspApi)
        ksp(kspProcessor)
    }

    with(libs) {
        // Dagger
        kapt(dagger.compiler)
        kapt(dagger.android.processor)

        // Retrofit
        impl(retrofit)
        impl(converter.gson)
    }
}