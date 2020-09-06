package com.tsarsprocket.reportmid

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
import com.tsarsprocket.reportmid.databinding.FragmentLandingBinding
import com.tsarsprocket.reportmid.databinding.FragmentLandingBindingImpl
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LandingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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

        viewModel.stateLive.observe( { lifecycle } ) {
            when( it ) {
                LandingViewModel.STATE.FOUND -> {
                    val action = LandingFragmentDirections.actionLandingFragmentToProfileOverviewFragment( viewModel.puuid )
                    findNavController().navigate( action )
                }
                LandingViewModel.STATE.NOT_FOUND -> {
                    val action = LandingFragmentDirections.actionLandingFragmentToInitialEnterFragment()
                    findNavController().navigate( action )
                }
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = LandingFragment()
    }
}