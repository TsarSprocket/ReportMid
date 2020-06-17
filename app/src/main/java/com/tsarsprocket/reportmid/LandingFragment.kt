package com.tsarsprocket.reportmid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.databinding.FragmentLandingBindingImpl
import kotlinx.android.synthetic.main.activity_main.view.*
import javax.inject.Inject

class LandingFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by activityViewModels<LandingViewModel> { viewModelFactory }

    lateinit var binding: FragmentLandingBindingImpl

    lateinit var masteryGroups: List<ViewGroup>

    override fun onAttach(context: Context) {

        (context.applicationContext as ReportMidApp).comp.inject(this)

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
                LandingViewModel.Status.UNVERIFIED -> navController.navigate( NavGraphDirections.actionGlobalInitialEnterFragment() )
                LandingViewModel.Status.VERIFIED -> refreshScreen()
            }
        } )

        for( i: Int in 0 until TOP_MASTERIES_NUM ) {
            viewModel.championImages[ i ].observe( viewLifecycleOwner, Observer { b ->
                with( masteryGroups[ i ] ) {
                    findViewWithTag<ImageView>( getString( R.string.fragment_landing_tag_champion_icon ) ).setImageBitmap( b )
                    findViewWithTag<TextView>( getString( R.string.fragment_landing_tag_champion_name ) ).text = viewModel.activeSummonerModel.value!!.masteries[ i ].champion.name
                }
            } )
        }
    }

    private fun refreshScreen() {
        binding.root.findViewById<ImageView>(R.id.imgSummonerIcon)
            .setImageBitmap(viewModel.activeSummonerModel.value?.icon)

        val masteryViewGroup = binding.root.findViewById<ViewGroup>(R.id.grpOtherChampMasteries)

        val summonerModel = viewModel.activeSummonerModel.value

        if (summonerModel != null) {
            val extraSumSize =
                if (summonerModel.masteries.size < TOP_MASTERIES_NUM) summonerModel.masteries.size else TOP_MASTERIES_NUM

            masteryGroups = List<ViewGroup>(extraSumSize) { i ->
                val mastery = summonerModel.masteries[i]
                val viewGroup =
                    if (i <= 0) binding.root.findViewById<ViewGroup>(R.id.grpMainChampMastery)
                    else (layoutInflater.inflate(
                        R.layout.champion_mastery,
                        null,
                        false
                    ) as ViewGroup).also { masteryViewGroup.addView(it) }
                viewGroup.findViewById<TextView>(R.id.txtChampLevel).text = mastery.level.toString()
                viewGroup.findViewById<TextView>(R.id.txtChampPoints).text =
                    mastery.points.toString()
                return@List viewGroup
            }
        }

        requireActivity().findViewById<Toolbar>( R.id.toolbar ).title = getString( R.string.fragment_landing_title_template ).format( viewModel.activeSummonerModel.value!!.name )

        binding.invalidateAll()
    }

    companion object {
        @JvmStatic
        fun newInstance() = LandingFragment()
    }
}
