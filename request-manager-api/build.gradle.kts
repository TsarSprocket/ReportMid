import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(namespace = "com.tsarsprocket.reportmid.request_manager_api") {
    impl(project(":lazy-proxy-ksp"))
    ksp(project(":lazy-proxy-ksp"))
}
