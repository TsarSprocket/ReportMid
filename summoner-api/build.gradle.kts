import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.library

library(namespace = "com.tsarsprocket.reportmid.summoner_api") {

    api(project(":base-api"))
    api(project(":lol"))

    // Rx
    impl(libs.rxandroid)
}
