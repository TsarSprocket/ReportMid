import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(namespace = "com.tsarsprocket.reportmid.request_manager_impl") {
    api(project(":base"))
    api(project(":request-manager-api"))
    api(project(":app-api"))

    impl(project(":lazy-proxy-ksp"))
    ksp(project(":lazy-proxy-ksp"))

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)
}
