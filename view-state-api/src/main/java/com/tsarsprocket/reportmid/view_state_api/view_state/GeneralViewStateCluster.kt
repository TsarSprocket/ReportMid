package com.tsarsprocket.reportmid.view_state_api.view_state

sealed class GeneralViewStateCluster : ViewState {

    override val clusterClass = GeneralViewStateCluster::class

    object Initial : GeneralViewStateCluster()
}