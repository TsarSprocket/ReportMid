package com.tsarsprocket.reportmid.viewStateApi.view

import androidx.compose.material3.SnackbarHostState
import androidx.fragment.app.Fragment
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent

abstract class ViewStateFragment : Fragment() {
    abstract val snackbarHostState: SnackbarHostState
    abstract fun postIntent(viewIntent: ViewIntent)
}
