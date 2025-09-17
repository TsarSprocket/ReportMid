package com.tsarsprocket.reportmid.matchHistory.impl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.api.model.Region
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

@Parcelize
@State
internal data class ShowingMatchHistoryState(
    override val puuid: String,
    override val region: Region,
    val matches: ImmutableList<MatchInfo> = persistentListOf(),
    val isLoading: Boolean,
    val canLoadMore: Boolean = true,
) : InternalMatchHistoryState {
    val itemsInList: Int
        get() = if(canLoadMore) matches.size.inc() else matches.size

    fun getItemToShow(index: Int): ItemToShow {
        return when {
            index in matches.indices -> matches[index]
            canLoadMore && index == matches.size -> LoadingMoreItem
            else -> throw IndexOutOfBoundsException()
        }
    }
}