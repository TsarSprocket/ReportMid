import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.dataDragonRoom",
) {
    impl(libs.androidx.core.ktx)
    impl(libs.androidx.appcompat)
    impl(libs.material)

    // Room
    impl(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    impl(libs.androidx.room.ktx)
    impl(libs.androidx.room.rxjava2)
}
