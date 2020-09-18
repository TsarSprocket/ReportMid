package com.tsarsprocket.reportmid.controller

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tsarsprocket.reportmid.BaseFragment
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.databinding.FragmentManageMySummonersBinding
import com.tsarsprocket.reportmid.viewmodel.ManageMySummonersViewModel
import javax.inject.Inject

class ManageMySummonersFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ManageMySummonersViewModel> { viewModelFactory }

    lateinit var binding: FragmentManageMySummonersBinding

    override fun onAttach(context: Context) {
        (context.applicationContext as ReportMidApp).comp.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_my_summoners, container, false)
        binding.lifecycleOwner = this

        baseActivity.toolbar.title = getString(R.string.fragment_manage_my_summoners_title)

        return binding.root
    }

    companion object {
        fun newInstance() = ManageMySummonersFragment()
    }

}