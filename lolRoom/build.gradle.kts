import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.lolRoom",
) {
    api(projects.lol)

    // Rx
    impl(libs.rxandroid)

    // Room
    api(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
}