package com.tsarsprocket.reportmid.appBar.impl.viewState

import android.os.Parcel
import androidx.compose.runtime.mutableStateOf
import com.tsarsprocket.reportmid.appBar.api.viewIntent.Iconized
import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.viewStateApi.backstack.BackStackEntry
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.parcelableCreator


@Parcelize
@State
internal class AppBarState(initialItems: ImmutableList<NavigationItem>) : ViewState {

    val items = mutableStateOf<ImmutableList<NavigationItem>>(initialItems)

    constructor() : this(persistentListOf())

    override fun start(holder: ViewStateHolder) {
        holder.stateCoroutineScope.launch {
            holder.backStack.entriesFlow.collect {
                items.value = makeItems(it).toPersistentList()
            }
        }
    }

    private fun makeItems(entries: List<BackStackEntry>): List<NavigationItem> {
        return entries.mapNotNull {
            (it.viewIntent as? Iconized)?.let { iconizedIntent ->
                NavigationItem(it.uuid, iconizedIntent.iconUrl)
            }
        }
    }

    companion object : Parceler<AppBarState> {

        override fun AppBarState.write(parcel: Parcel, flags: Int) {
            parcel.writeTypedList(items.value)
        }

        override fun create(parcel: Parcel): AppBarState = AppBarState(parcel.createTypedArrayList(parcelableCreator<NavigationItem>()).orEmpty().toPersistentList())
    }
}
