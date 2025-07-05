import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.lol.impl",
) {
    with(projects) {
        api(lol.api)

        impl(appApi)
        impl(baseApi)
        impl(utils)

        impl(kspApi)
        ksp(kspProcessor)
    }

    with(libs) {
        // Dagger
        ksp(dagger.compiler)
        ksp(dagger.android.processor)
    }
}
