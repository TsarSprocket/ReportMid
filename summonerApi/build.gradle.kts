import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.library

library(namespace = "com.tsarsprocket.reportmid.summonerApi") {

    api(projects.baseApi)
    api(projects.lol)

    // Rx
    impl(libs.rxandroid)
}
