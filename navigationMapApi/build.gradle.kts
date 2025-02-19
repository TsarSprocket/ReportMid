import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.navigationMapApi",
) {
    api(projects.viewStateApi)
    api(projects.landingApi)
    api(projects.findSummonerApi)
}