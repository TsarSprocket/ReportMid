package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.*
import com.tsarsprocket.reportmid.databinding.FragmentLandingBindingImpl
import com.tsarsprocket.reportmid.viewmodel.LandingViewModel
import javax.inject.Inject

class LandingFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel by viewModels<LandingViewModel> { viewModelFactory }

    lateinit var binding: FragmentLandingBindingImpl

    // Methods

    override fun onAttach(context: Context) {
        (context.applicationContext as ReportMidApp).comp.inject( this )

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_landing, container, false )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        getNavigationResult<String>( RESULT_PUUID ).observe( { lifecycle } ) { puuid ->
            viewModel.defineMainAccount( puuid ).observe( { lifecycle } ) { navigateToMainAndFinish( puuid ) }
        }

        viewModel.stateLive.observe( { lifecycle } ) {
            when( it ) {
                LandingViewModel.STATE.FOUND -> {
                    navigateToMainAndFinish( viewModel.puuid )
                }
                LandingViewModel.STATE.NOT_FOUND -> {
                    if( peekNavigationResult<String>( RESULT_PUUID ) == null && !viewModel.beenThereDoneThat ) {
                        val action = LandingFragmentDirections.actionManageMySummonersFragmentToAddSummonerGraph()
                        findNavController().navigate(action)
                        viewModel.beenThereDoneThat = true
                    }
                }
            }
        }

        if( viewModel.beenThereDoneThat ) requireActivity().finish()

        return binding.root
    }

    private fun navigateToMainAndFinish( puuid: String ) {
        val action = LandingFragmentDirections.actionLandingFragmentToMainActivity( puuid )
        findNavController().navigate( action )
        requireActivity().finish()
    }

    companion object {
        @JvmStatic
        fun newInstance() = LandingFragment()
    }
}