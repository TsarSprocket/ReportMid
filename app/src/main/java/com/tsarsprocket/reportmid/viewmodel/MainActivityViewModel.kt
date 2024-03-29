package com.tsarsprocket.reportmid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.R
import javax.inject.Inject

val TOOLBAR_MENU_BY_DESTINATION = mapOf(
    R.id.matchupFragment to R.menu.matchup_toolbar,
    R.id.manageMySummonersFragment to R.menu.manage_my_summoners_toolbar,
    R.id.manageFriendsFragment to R.menu.manage_my_friends_toolbar,
)

class MainActivityViewModel @Inject constructor() : ViewModel() {

    private val mutableToolbarMenuByDestination = HashMap( TOOLBAR_MENU_BY_DESTINATION )

    val toolbarMenuByDestination = mutableToolbarMenuByDestination.toMap()

    val updateToolbarMenuForDestination = MutableLiveData<Int>()

    val selectedMenuItem = MutableLiveData<Int>()
    val menuRefreshed = MutableLiveData(true)

    //  Methods  //////////////////////////////////////////////////////////////

    fun setToolbarMenuForDestination(destinationId: Int, menuId: Int) {
        mutableToolbarMenuByDestination[destinationId] = menuId
        updateToolbarMenuForDestination.value = destinationId
    }

    fun clearToolbarMenuForDestination(destinationId: Int) {
        mutableToolbarMenuByDestination.remove(destinationId)
        updateToolbarMenuForDestination.value = destinationId
    }
}