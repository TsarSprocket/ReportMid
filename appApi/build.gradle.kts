import com.tsarsprocket.reportmid.gradle.api
import com.tsarsprocket.reportmid.gradle.kapt
import com.tsarsprocket.reportmid.gradle.library

library(
    namespace = "com.tsarsprocket.reportmid.appApi",
) {
    api(projects.baseApi)
    api(projects.viewStateApi)
    api(projects.dataDragonRoom)
    api(projects.lolRoom)
    api(projects.summonerRoom)
    api(projects.stateRoom)

    // Room
    api(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    api(libs.androidx.room.ktx)
}