package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tsarsprocket.reportmid.ARG_REGION
import com.tsarsprocket.reportmid.BaseFragment
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.RESULT_CONFIRM
import com.tsarsprocket.reportmid.RESULT_PUUID_AND_REG
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.baseApi.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.databinding.FragmentAddSummonerBinding
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.summonerApi.model.Summoner
import com.tsarsprocket.reportmid.tools.OneTimeObserver
import com.tsarsprocket.reportmid.tools.peekNavigationReturnedValue
import com.tsarsprocket.reportmid.tools.setNavigationResult
import com.tsarsprocket.reportmid.tools.setSoftInputVisibility
import com.tsarsprocket.reportmid.viewmodel.AddSummonerViewModel
import io.reactivex.Maybe
import java.util.Formatter
import javax.inject.Inject

class AddSummonerFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<AddSummonerViewModel> { viewModelFactory }

    lateinit var binding: FragmentAddSummonerBinding

    var bringUpKeyboard = true

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
            val reg = Region.byTag[forceRegTag] ?: throw RuntimeException("Illegal region tag: $forceRegTag")
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
            bringUpKeyboard = false
            val summonerModel = viewModel.activeSummoner.value!!
            setNavigationResult(result = PuuidAndRegion(summonerModel.puuid,summonerModel.region), key = RESULT_PUUID_AND_REG)
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (bringUpKeyboard) setSoftInputVisibility(requireContext(), binding.edSummonerName, true)
    }

    fun onValidateInitial(view: View?) {
        setSoftInputVisibility(requireContext(), binding.edSummonerName, false)

        object : OneTimeObserver<Maybe<Summoner>>() {
            override fun onOneTimeChanged(maybe: Maybe<Summoner>) {
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

