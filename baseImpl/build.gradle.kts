import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.baseImpl",
) {
    with(projects) {
        api(baseApi)
        api(appApi)

        impl(kspApi)
        ksp(kspProcessor)
    }

    with(libs) {
        // Dagger
        kapt(dagger.compiler)
        kapt(dagger.android.processor)
    }
}