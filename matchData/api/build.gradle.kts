import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.matchData.api",
) {
    api(projects.baseApi)
    api(projects.lol)
}