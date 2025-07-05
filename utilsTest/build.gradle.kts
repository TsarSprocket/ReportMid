import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.javaLibrary

javaLibrary {
    with(libs) {
        impl(junit.jupiter.api)

        impl(kotlinx.coroutines.core)
        impl(kotlinx.coroutines.test)
    }
}
