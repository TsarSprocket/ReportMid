package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.BaseFragment
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.RESULT_PUUID_AND_REG
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.baseApi.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.databinding.FragmentLandingBindingImpl
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.tools.doesReturnedValueExist
import com.tsarsprocket.reportmid.tools.prepareNavigationReturnedValue
import com.tsarsprocket.reportmid.tools.removeNavigationReturnedValue
import com.tsarsprocket.reportmid.viewmodel.LandingViewModel
import javax.inject.Inject

class LandingFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val viewModel by viewModels<LandingViewModel> { viewModelFactory }

    lateinit var binding: FragmentLandingBindingImpl

    // Methods

    override fun onAttach(context: Context) {
        (context.applicationContext as ReportMidApp).comp.inject(this)

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_landing, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        if (doesReturnedValueExist(RESULT_PUUID_AND_REG)) {
            val puuidAndReg = removeNavigationReturnedValue<PuuidAndRegion>(RESULT_PUUID_AND_REG)
            if (puuidAndReg != null) {
                viewModel.defineMainAccount(puuidAndReg).observe(viewLifecycleOwner) { navigateToMainAndFinish(puuidAndReg) }
            } else {
                requireActivity().finish()
            }
        } else {
            viewModel.stateLive.observe(viewLifecycleOwner) {
                when (it) {
                    LandingViewModel.STATE.FOUND -> {
                        navigateToMainAndFinish(viewModel.puuid)
                    }
                    LandingViewModel.STATE.NOT_FOUND -> {
                        prepareNavigationReturnedValue(null, RESULT_PUUID_AND_REG)
                        val action = LandingFragmentDirections.actionManageMySummonersFragmentToAddSummonerGraph()
                        findNavController().navigate(action)
                    }
                    else -> {
                    }
                }
            }
        }
        return binding.root
    }

    private fun navigateToMainAndFinish(puuid: PuuidAndRegion) {
        val action = LandingFragmentDirections.actionLandingFragmentToMainActivity(puuid)
        findNavController().navigate(action)
        requireActivity().finish()
    }

    companion object {
        @JvmStatic
        fun newInstance() = LandingFragment()
    }
}