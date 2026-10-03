plugins {
    id("reportmid.android.application")
}

reportMidApp {
    appId = "com.tsarsprocket.reportmid.app"
    namespace = "com.tsarsprocket.reportmid.appImpl"
}

dependencies {
    api(projects.appApi)

    implementation(projects.baseImpl)
    implementation(projects.currentGameData.impl)
    implementation(projects.dataDragonImpl)
    implementation(projects.findSummonerImpl)
    implementation(projects.landingImpl)
    implementation(projects.leaguePositionImpl)
    implementation(projects.lol.impl)
    implementation(projects.lolServicesImpl)
    implementation(projects.mainScreenImpl)
    implementation(projects.matchData.impl)
    implementation(projects.matchDetails.impl)
    implementation(projects.matchHistory.impl)
    implementation(projects.matchUpView.impl)
    implementation(projects.navigationMapImpl)
    implementation(projects.profileOverviewImpl)
    implementation(projects.requestManagerImpl)
    implementation(projects.stateImpl)
    implementation(projects.summonerImpl)
    implementation(projects.summonerViewImpl)
    implementation(projects.viewStateImpl)

    implementation(projects.kspApi)
    ksp(projects.kspProcessor)

    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.ui.main)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    debugImplementation(libs.compose.ui.tooling.main)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Dagger
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    // Room
    api(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    api(libs.androidx.room.ktx)
}
