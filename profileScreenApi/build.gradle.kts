import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.profileScreenApi",
) {
    api(projects.baseApi)
    api(projects.viewStateApi)
}