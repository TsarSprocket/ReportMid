package com.tsarsprocket.reportmid.viewStateApi.view

import androidx.compose.material3.SnackbarHostState
import androidx.fragment.app.Fragment

abstract class ViewStateFragment : Fragment() {

    abstract val snackbarHostState: SnackbarHostState

    companion object {
        const val INITIAL_STATE = "INITIAL_STATE"
        const val START_INTENT = "START_INTENT"
    }
}
