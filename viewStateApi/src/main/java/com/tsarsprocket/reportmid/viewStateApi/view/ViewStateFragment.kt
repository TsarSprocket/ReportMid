package com.tsarsprocket.reportmid.viewStateApi.view

import androidx.fragment.app.Fragment

abstract class ViewStateFragment : Fragment() {

    companion object {
        const val INITIAL_STATE = "INITIAL_STATE"
        const val START_INTENT = "START_INTENT"
    }
}
