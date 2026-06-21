import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.navigationMapApi",
) {
    with(projects) {
        api(findSummonerApi)
        api(landingApi)
        api(mainScreenApi)
        api(matchDetails.api)
        api(matchHistory.api)
        api(matchUpView.api)
        api(summonerViewApi)
        api(viewStateApi)
    }
}