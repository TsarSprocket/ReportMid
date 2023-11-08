package com.tsarsprocket.reportmid.view_state_api.view_state

sealed class GeneralViewEffectCluster : ViewEffect {
    override val clusterClass = GeneralViewEffectCluster::class

    object GoBack : GeneralViewEffectCluster()
}