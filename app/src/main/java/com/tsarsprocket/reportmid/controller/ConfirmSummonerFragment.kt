package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.*
import com.tsarsprocket.reportmid.databinding.FragmentConfirmSummonerBinding
import com.tsarsprocket.reportmid.viewmodel.ConfirmSummonerViewModel
import kotlinx.android.synthetic.main.fragment_confirm_summoner.view.*
import java.lang.IllegalArgumentException
import javax.inject.Inject

class ConfirmSummonerFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ConfirmSummonerViewModel> { viewModelFactory }

    private lateinit var binding: FragmentConfirmSummonerBinding

    override fun onAttach(context: Context) {
        (context.applicationContext as ReportMidApp).comp.inject( this )
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_confirm_summoner, container, false )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.bitmap.observe( { lifecycle } ) { bitmap -> binding.root.imgSummonerIcon.setImageBitmap( bitmap ) }
        viewModel.confirm.observe( { lifecycle } ) { confirmed -> setNavigationResult( confirmed, RESULT_CONFIRM ); findNavController().popBackStack() }

        viewModel.init( arguments?.getString( ARG_PUUID )?: throw IllegalArgumentException( "Fragment ${javaClass.kotlin.simpleName} requires $ARG_PUUID argument" ) )

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance( puuid: String ) =
            ConfirmSummonerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PUUID, puuid)
                }
            }
    }
}