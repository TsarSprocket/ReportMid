import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.debug
import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.utils",
    enableCompose = true,
) {
    impl(projects.theme)

    with(libs) {
        impl(androidx.core.ktx)
        impl(androidx.appcompat)

        // Material
        impl(material)

        // Dagger
        api(dagger.main)

        // Compose
        impl(platform(compose.bom))

        // Compose Material 3
        impl(compose.material3)

        // Compose preview support
        impl(compose.ui.tooling.preview)
        debug(compose.ui.tooling.main)
    }
}