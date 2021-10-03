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
import com.tsarsprocket.reportmid.*
import com.tsarsprocket.reportmid.databinding.FragmentConfirmSummonerBinding
import com.tsarsprocket.reportmid.model.PuuidAndRegion
import com.tsarsprocket.reportmid.tools.setNavigationResult
import com.tsarsprocket.reportmid.viewmodel.ConfirmSummonerViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

class ConfirmSummonerFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ConfirmSummonerViewModel> { viewModelFactory }

    private lateinit var binding: FragmentConfirmSummonerBinding

    override fun onAttach(context: Context) {
        (context.applicationContext as ReportMidApp).comp.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_summoner, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.bitmap.observe(viewLifecycleOwner) { icon -> binding.imgSummonerIcon.setImageDrawable(icon) }
        viewModel.confirm.observe(viewLifecycleOwner) { confirmed -> setNavigationResult(confirmed, RESULT_CONFIRM); findNavController().popBackStack() }

        viewModel.init(
            arguments?.getParcelable(ARG_PUUID_AND_REG)
                ?: throw IllegalArgumentException("Fragment ${javaClass.kotlin.simpleName} requires $ARG_PUUID_AND_REG argument")
        )

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(puuidAndRegion: PuuidAndRegion) =
            ConfirmSummonerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PUUID_AND_REG, puuidAndRegion)
                }
            }
    }
}