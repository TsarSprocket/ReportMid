import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.currentGameData.api",
) {
    with(projects) {
        api(baseApi)
        api(lol.api)
    }
}