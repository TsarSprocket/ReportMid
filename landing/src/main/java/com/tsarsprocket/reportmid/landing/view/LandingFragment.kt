package com.tsarsprocket.reportmid.landing.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tsarsprocket.reportmid.base.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.landing.capability.LandingCapability
import com.tsarsprocket.reportmid.landing.viewmodel.LandingViewModel
import javax.inject.Inject

class LandingFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: LandingViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        LandingCapability.component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}