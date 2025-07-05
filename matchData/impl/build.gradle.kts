import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library
import com.tsarsprocket.reportmid.gradle.test
import com.tsarsprocket.reportmid.gradle.testRuntimeOnly

library(
    namespace = "com.tsarsprocket.reportmid.matchData.impl",
) {
    with(projects) {
        api(matchData.api)

        impl(appApi)
        impl(baseApi)
        impl(dataDragonApi)
        impl(lol.api)
        impl(lolServicesApi)
        impl(requestManagerApi)
        impl(utils)

        // KSP
        impl(kspApi)
        ksp(kspProcessor)

        test(utilsTest)
    }

    with(libs) {
        impl(androidx.core.ktx)

        impl(mayakapps.kache)

        // Dagger
        kapt(dagger.compiler)
        kapt(dagger.android.processor)

        // Retrofit
        impl(retrofit)
        impl(converter.gson)

//        test(kotlin("test"))

        // JUnit 5
        test(junit.jupiter.api)
        testRuntimeOnly(junit.platform.launcher)
        testRuntimeOnly(junit.jupiter.engine)

        // Mockito
        test(mockito.core)
        test(mockito.kotlin)

        test(kotlinx.coroutines.test)
    }
}