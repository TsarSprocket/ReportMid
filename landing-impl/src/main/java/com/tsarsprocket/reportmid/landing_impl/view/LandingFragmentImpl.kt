package com.tsarsprocket.reportmid.landing_impl.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import com.tsarsprocket.reportmid.base.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.landing_api.view.LandingFragment
import com.tsarsprocket.reportmid.landing_impl.viewmodel.LandingViewModel
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import javax.inject.Inject

class LandingFragmentImpl @Inject constructor(
    private val viewModelFactory: ViewModelFactory,
) : LandingFragment() {

    private val viewModel: LandingViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            updateLayoutParams {
                width = MATCH_PARENT
                height = WRAP_CONTENT
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (view as ComposeView).setContent {
            ReportMidTheme {
                Landing()
            }
        }
    }
}