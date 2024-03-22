import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.library
import com.tsarsprocket.reportmid.gradle.test

library(namespace = "com.tsarsprocket.reportmid.summoner_room") {

    impl(project(":lol-room"))

    // Room
    impl(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    impl(libs.androidx.room.ktx)
    impl(libs.androidx.room.rxjava2)
    test(libs.androidx.room.testing)
}
