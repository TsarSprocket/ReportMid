package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tsarsprocket.reportmid.*
import com.tsarsprocket.reportmid.databinding.FragmentAddSummonerBinding
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.viewmodel.AddSummonerViewModel
import kotlinx.android.synthetic.main.fragment_add_summoner.view.*
import java.util.*
import javax.inject.Inject

class AddSummonerFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<AddSummonerViewModel> { viewModelFactory }

    lateinit var binding: FragmentAddSummonerBinding

    override fun onAttach(context: Context) {
        (context.applicationContext as ReportMidApp).comp.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_summoner, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.fragment = this

        val forceRegTag = arguments?.getString(ARG_REGION)
        if (forceRegTag != null) {
            val reg = RegionModel.byTag[forceRegTag] ?: throw RuntimeException("Illegal region tag: $forceRegTag")
            viewModel.selectedRegionPosition.value = viewModel.allRegions.indexOf(reg)
            binding.spRegion.visibility = View.GONE
            binding.txtRegion.visibility = View.VISIBLE
        } else {
            binding.spRegion.visibility = View.VISIBLE
            binding.txtRegion.visibility = View.GONE
        }

        binding.edSummonerName.requestFocus()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSoftInputVisibility(requireContext(), binding.edSummonerName, true)

        getNavigationResult<Boolean>(RESULT_CONFIRM)
            .switchMap { confirmed ->
                if (confirmed) {
                    viewModel.activeSummonerModel
                } else MutableLiveData()
            }
            .observe({ lifecycle }) { summonerModel ->
                setNavigationResult(result = summonerModel.puuid, key = RESULT_PUUID)
                findNavController().popBackStack()
            }
    }

    fun onValidateInitial(view: View?) {
        setSoftInputVisibility(requireContext(), binding.root.edSummonerName, false)
        viewModel.checkSummoner().observe({ lifecycle }) { maybe ->
            if (maybe.isEmpty.blockingGet()) {
                viewModel.activeSummonerName.observe( this ) { summonerName ->
                    Snackbar.make(
                        binding.root,
                        Formatter().format(getString(R.string.snack_summoner_not_found), summonerName).toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            } else {
                val action = AddSummonerFragmentDirections.actionAddSummonerFragmentToConfirmSummonerFragment(maybe.blockingGet().puuid)
                findNavController().navigate(action)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddSummonerFragment()
    }
}

