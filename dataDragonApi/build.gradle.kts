import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.dataDragonApi",
) {
    api(projects.baseApi)
    api(projects.lol.api)

    // Rx
    impl(libs.rxandroid)

    impl(libs.androidx.core.ktx)
    impl(libs.androidx.appcompat)
    impl(libs.material)
}
