import com.tsarsprocket.reportmid.gradle.impl
import com.tsarsprocket.reportmid.gradle.ksp
import com.tsarsprocket.reportmid.gradle.library

library(namespace = "com.tsarsprocket.reportmid.requestManagerApi") {
    impl(projects.kspProcessor)
    ksp(projects.kspProcessor)
}
