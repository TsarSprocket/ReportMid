import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.matchDetails.api",
) {
    with(projects) {
        impl(lol.api)
        impl(viewStateApi)
    }
}