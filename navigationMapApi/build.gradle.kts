import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.navigationMapApi",
) {
    with(projects) {
        api(findSummonerApi)
        api(landingApi)
        api(mainScreenApi)
        api(summonerViewApi)
        api(viewStateApi)
    }
}