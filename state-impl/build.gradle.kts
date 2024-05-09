import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.library

library(namespace = "com.tsarsprocket.reportmid.state_impl") {
    api(project(":base-api"))
    api(project(":state-api"))
    api(project(":state-room"))
    api(project(":app-api"))

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)
}