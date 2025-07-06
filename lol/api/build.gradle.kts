import com.tsarsprocket.reportmid.gradle.androidTest
import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library
import com.tsarsprocket.reportmid.gradle.test
import com.tsarsprocket.reportmid.gradle.testRuntimeOnly

library(
    namespace = "com.tsarsprocket.reportmid.lol.api",
    enableCompose = true,
) {
    with(projects) {
        api(utils)

        impl(kspApi)
        ksp(kspProcessor)
    }

    with(libs) {
        impl(androidx.core.ktx)
        impl(androidx.appcompat)
        impl(compose.runtime)
        impl(material)

        test(mockito.core)
        test(mockito.kotlin)

        // JUnit
        test(platform(junit.bom))
        test(junit.vintage.api)
        testRuntimeOnly(junit.vintage.engine)

        androidTest(androidx.junit)
        androidTest(androidx.espresso.core)
    }
}
