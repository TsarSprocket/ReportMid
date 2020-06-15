package com.tsarsprocket.reportmid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.databinding.FragmentLandingBindingImpl
import javax.inject.Inject

private const val NUM_OF_SEC_MASTERIES = 4

class LandingFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by activityViewModels<LandingViewModel> { viewModelFactory }

    lateinit var binding: FragmentLandingBindingImpl

    lateinit var masteryGroups: List<ViewGroup>

    override fun onAttach( context: Context ) {

        ( context.applicationContext as ReportMidApp ).comp.inject( this )

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentLandingBindingImpl>( inflater, R.layout.fragment_landing, container, false )
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        viewModel.state.observe( viewLifecycleOwner, Observer { state ->
            when( state ) {
                LandingViewModel.Status.UNVERIFIED -> navController.navigate( R.id.initialEnterFragment )
                LandingViewModel.Status.VERIFIED -> refreshScreen()
            }
        } )

        for( i: Int in 0 NUM_OF_SEC_MASTERIES ) {
            viewModel.championImages[ i ].observe( viewLifecycleOwner, Observer { b ->
                masteryGroups[ i ].findViewWithTag<ImageView>( getString( R.string.fragment_landing_tag_champion_icon ) ).setImageBitmap( b )
            } )
        }
    }

    private fun refreshScreen() {
        binding.invalidateAll()
        binding.root.findViewById<ImageView>( R.id.imgSummonerIcon ).setImageBitmap( viewModel.activeSummonerModel.value?.icon )

        val masteryViewGroup = binding.root.findViewById<ViewGroup>( R.id.grpOtherChampMasteries )

        val summonerModel = viewModel.activeSummonerModel.value

        if( summonerModel != null ) {
            val extraSumSize = if( summonerModel.masteries.size < NUM_OF_SEC_MASTERIES ) summonerModel.masteries.size else NUM_OF_SEC_MASTERIES

            masteryGroups = List<ViewGroup>( extraSumSize ) { i ->
                val mastery = summonerModel.masteries[ i ]
                val viewGroup = if (i <= 0) binding.root.findViewById<ViewGroup>(R.id.grpMainChampMastery)
                    else ( layoutInflater.inflate( R.layout.champion_mastery, masteryViewGroup, true ) as ViewGroup)
                viewGroup.findViewById<TextView>(R.id.txtChampLevel).text = mastery.level.toString()
                viewGroup.findViewById<TextView>(R.id.txtChampPoints).text = mastery.points.toString()
                return@List viewGroup
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LandingFragment()
    }
}
