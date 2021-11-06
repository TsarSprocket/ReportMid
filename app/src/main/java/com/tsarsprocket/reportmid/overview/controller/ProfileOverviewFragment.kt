package com.tsarsprocket.reportmid.overview.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.ARG_PUUID_AND_REG
import com.tsarsprocket.reportmid.BaseFragment
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.databinding.ChampionMasteryBinding
import com.tsarsprocket.reportmid.databinding.FragmentProfileOverviewBinding
import com.tsarsprocket.reportmid.model.PuuidAndRegion
import com.tsarsprocket.reportmid.overview.viewmodel.ProfileOverviewViewModel
import com.tsarsprocket.reportmid.viewmodel.TOP_MASTERIES_NUM
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ProfileOverviewFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ProfileOverviewViewModel> { viewModelFactory }

    lateinit var binding: FragmentProfileOverviewBinding

    override fun onAttach( context: Context ) {
        (context.applicationContext as ReportMidApp).comp.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileOverviewBinding.inflate( inflater, container, false )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated( view: View, savedInstanceState: Bundle? ) {

        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            val puuidAndReg: PuuidAndRegion =
                requireArguments().getParcelable( ARG_PUUID_AND_REG ) ?: throw IllegalArgumentException( "Missing PUUID argument" )
            viewModel.puuidAndRegion.emit( puuidAndReg )
            viewModel.getMasteriesFlow().collect { modelsList ->
                binding.grpOtherChampMasteries.removeAllViews()
                modelsList.take( TOP_MASTERIES_NUM ).forEach { model ->
                    val masteryViewBinding = ChampionMasteryBinding.inflate( layoutInflater, binding.grpOtherChampMasteries, false )
                    launch { model.icon.take( 1 ).collect { drawable ->
                        masteryViewBinding.iconChampion.setImageDrawable( drawable )
                    } }
                    masteryViewBinding.txtChampLevel.text = model.level.toString()
                    masteryViewBinding.txtChampPoints.text = model.points.toString()
                    binding.grpOtherChampMasteries.addView( masteryViewBinding.root )
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.summoner.collect { summoner ->
                baseActivity.toolbar.title = summoner.name
            }
        }

        with( binding.bottomNavigation ) {
            setOnNavigationItemSelectedListener{ menuItem -> navigateToSibling( menuItem ) }
            selectedItemId = R.id.profileOverviewFragment
        }
    }

    private fun navigateToSibling(item: MenuItem ): Boolean {
        val navOptions = NavOptions.Builder().setLaunchSingleTop( true ).setPopUpTo(R.id.profileOverviewFragment, true ).build()
        return when( item.itemId ) {
            R.id.profileOverviewFragment -> true
            R.id.matchupFragment -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.summoner.take( 1 ).collect { summonerModel ->
                        val action = ProfileOverviewFragmentDirections.actionProfileOverviewFragmentToMatchupFragment(
                            PuuidAndRegion( summonerModel.puuid, summonerModel.region ) )
                        findNavController().navigate( action, navOptions )
                    }
                }
                true
            }
            R.id.matchHistoryFragment -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.summoner.take( 1 ).collect { summonerModel ->
                        val action = ProfileOverviewFragmentDirections.actionProfileOverviewFragmentToMatchHistoryFragment(
                            PuuidAndRegion( summonerModel.puuid, summonerModel.region ) )
                        findNavController().navigate( action, navOptions )
                    }
                }
                true
            }
            else -> false
        }
    }

    companion object {
        fun newInstance( puuid: String ) = ProfileOverviewFragment().apply {
            arguments = Bundle( 1 ).apply { putString( ARG_PUUID_AND_REG, puuid ) }
        }
    }
}
