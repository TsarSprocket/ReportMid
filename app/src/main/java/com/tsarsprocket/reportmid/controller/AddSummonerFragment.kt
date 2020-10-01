package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tsarsprocket.reportmid.*
import com.tsarsprocket.reportmid.databinding.FragmentAddSummonerBinding
import com.tsarsprocket.reportmid.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.tools.*
import com.tsarsprocket.reportmid.viewmodel.AddSummonerViewModel
import io.reactivex.Maybe
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

        val confirmed = peekNavigationReturnedValue<Boolean>(RESULT_CONFIRM)
        if (confirmed != null && confirmed) {
            binding.root.edSummonerName.requestFocus()
            setSoftInputVisibility(requireContext(), binding.root.edSummonerName, false)
            val summonerModel = viewModel.activeSummonerModel.value!!
            setNavigationResult(result = PuuidAndRegion(summonerModel.puuid,summonerModel.region), key = RESULT_PUUID_AND_REG)
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSoftInputVisibility(requireContext(), binding.edSummonerName, true)
    }

    fun onValidateInitial(view: View?) {
        setSoftInputVisibility(requireContext(), binding.root.edSummonerName, false)

        object : OneTimeObserver<Maybe<SummonerModel>>() {
            override fun onOneTimeChanged(maybe: Maybe<SummonerModel>) {
                if (maybe.isEmpty.blockingGet()) {
                    viewModel.activeSummonerName.observe(this@AddSummonerFragment) { summonerName ->
                        Snackbar.make(
                            binding.root,
                            Formatter().format(getString(R.string.fragment_add_summoner_snack_summoner_not_found), summonerName).toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    val sum = maybe.blockingGet()

                    viewModel.isSummonerInUseLive(sum).observe(viewLifecycleOwner) { isInUse ->
                        if (isInUse) {
                            Snackbar.make(
                                binding.root,
                                Formatter().format(getString(R.string.fragment_add_summoner_snack_summoner_is_in_use), sum.name).toString(),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        } else {
                            val action = AddSummonerFragmentDirections.actionAddSummonerFragmentToConfirmSummonerFragment(PuuidAndRegion(sum.puuid, sum.region))
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }.observeOn(viewModel.checkSummoner(), this)

/*
        getNavigationReturnedValue<Boolean>(RESULT_CONFIRM)
            .switchMap { confirmed ->
                if (confirmed) {
                    viewModel.activeSummonerModel
                } else MutableLiveData()
            }
            .observe(viewLifecycleOwner) { summonerModel ->
                setSoftInputVisibility(requireContext(), binding.root.edSummonerName, false)
                setNavigationResult(result = summonerModel.puuid, key = RESULT_PUUID)
                findNavController().popBackStack()
            }
*/
    }

    companion object {
        @JvmStatic
        fun newInstance(regionTag: String? = null): AddSummonerFragment {
            return AddSummonerFragment().apply {
                if (regionTag != null) {
                    arguments = Bundle().apply {
                        putString(ARG_REGION, regionTag)
                    }
                }
            }
        }
    }
}

