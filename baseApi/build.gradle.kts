import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.baseApi",
) {
    api(projects.utils)

    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)
    api(libs.androidx.fragment.ktx)

    // Dagger
    api(libs.dagger.main)
    api(libs.dagger.android)
    api(libs.dagger.android.support)
}
