import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.stateImpl",
) {
    api(projects.baseApi)
    api(projects.stateApi)
    api(projects.stateRoom)
    api(projects.appApi)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)
}