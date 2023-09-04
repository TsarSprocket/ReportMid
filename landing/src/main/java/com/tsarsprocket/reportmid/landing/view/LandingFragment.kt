package com.tsarsprocket.reportmid.landing.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tsarsprocket.reportmid.base.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.landing.viewmodel.LandingViewModel
import javax.inject.Inject

class LandingFragment @Inject constructor(
    private val viewModelFactory: ViewModelFactory,
) : Fragment() {

    private val viewModel: LandingViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}