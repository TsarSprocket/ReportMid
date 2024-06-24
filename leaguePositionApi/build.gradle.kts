import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.leaguePositionApi",
) {
    api(projects.baseApi)
    api(projects.lol)
}