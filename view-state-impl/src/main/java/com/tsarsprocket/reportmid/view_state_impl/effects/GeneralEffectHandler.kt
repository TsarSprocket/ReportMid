package com.tsarsprocket.reportmid.view_state_impl.effects

import androidx.fragment.app.Fragment
import com.tsarsprocket.reportmid.view_state_api.view_state.EffectHandler
import com.tsarsprocket.reportmid.view_state_api.view_state.GeneralViewEffectCluster
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewStateHolder
import javax.inject.Inject

internal class GeneralEffectHandler @Inject constructor() : EffectHandler<GeneralViewEffectCluster> {

    override suspend fun handleEffect(effect: GeneralViewEffectCluster, fragment: Fragment, controller: ViewStateHolder) {
        fragment.requireActivity().onBackPressed()
    }
}