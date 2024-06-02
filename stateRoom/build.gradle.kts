import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.library

library(namespace = "com.tsarsprocket.reportmid.stateRoom") {
    api(projects.lolRoom)
    impl(libs.androidx.room.rxjava2)

    // Room
    impl(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
}