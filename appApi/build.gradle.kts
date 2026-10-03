plugins {
    id("reportmid.android.library")
}

reportMidLib {
    namespace = "com.tsarsprocket.reportmid.appApi"
}

dependencies {
    api(projects.baseApi)
    api(projects.viewStateApi)
    api(projects.dataDragonRoom)
    api(projects.lolRoom)
    api(projects.summonerRoom)
    api(projects.stateRoom)

    // Room
    api(libs.androidx.room.runtime)
    api(libs.androidx.room.ktx)
}
