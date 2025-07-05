import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.mainScreenApi",
) {
    api(projects.baseApi)
    api(projects.lol.api)
    api(projects.viewStateApi)
}