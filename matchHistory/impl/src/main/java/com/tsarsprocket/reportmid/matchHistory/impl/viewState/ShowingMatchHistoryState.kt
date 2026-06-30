package com.tsarsprocket.reportmid.matchHistory.impl.viewState

import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

@Parcelize
@State
internal data class ShowingMatchHistoryState(
    override val puuid: String,
    override val region: Region,
    val lastLoadedAt: Long = System.currentTimeMillis(),
    val matches: ImmutableList<ItemToShow> = persistentListOf(),
    val isLoading: Boolean,
    val canLoadMore: Boolean = true,
) : InternalMatchHistoryState {
    val itemsInList: Int
        get() = matches.size + if (canLoadMore) 1 else 0

    fun getItemToShow(index: Int): ItemToShow {
        return when (index) {
            in matches.indices -> matches[index]
            matches.size -> LoadingMoreItem
            else -> throw IndexOutOfBoundsException()
        }
    }
}
