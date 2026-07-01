package com.tsarsprocket.reportmid.appBar.impl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

internal const val MAX_APP_BAR_ICONS = 9

@Parcelize
@State
internal data class ShowingAppBarState(
    val iconUrls: ImmutableList<String> = persistentListOf(),
) : InternalAppBarViewState {
    init {
        require(iconUrls.size <= MAX_APP_BAR_ICONS) {
            "AppBar supports at most $MAX_APP_BAR_ICONS icons, got ${iconUrls.size}"
        }
    }
}
