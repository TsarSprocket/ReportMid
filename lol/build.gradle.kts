import com.tsarsprocket.reportmid.gradle.androidTest
import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library
import com.tsarsprocket.reportmid.gradle.test

library(
    namespace = "com.tsarsprocket.reportmid.lol",
) {
    api(projects.utils)

    impl(projects.kspProcessor)
    ksp(projects.kspProcessor)

    api(libs.orianna.android) // to be removed after the migration to RIOT API

    impl(libs.androidx.core.ktx)
    impl(libs.androidx.appcompat)
    impl(libs.material)
    test(libs.junit)
    test(libs.mockito.core)
    test(libs.mockito.kotlin)
    androidTest(libs.androidx.junit)
    androidTest(libs.androidx.espresso.core)
}
