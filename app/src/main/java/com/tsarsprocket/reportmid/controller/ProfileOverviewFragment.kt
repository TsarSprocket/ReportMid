package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.*
import com.tsarsprocket.reportmid.databinding.FragmentProfileOverviewBindingImpl
import com.tsarsprocket.reportmid.model.PuuidAndRegion
import com.tsarsprocket.reportmid.tools.formatPoints
import com.tsarsprocket.reportmid.viewmodel.ProfileOverviewViewModel
import com.tsarsprocket.reportmid.viewmodel.TOP_MASTERIES_NUM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_profile_overview.view.*
import javax.inject.Inject

class ProfileOverviewFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ProfileOverviewViewModel> { viewModelFactory }

    lateinit var binding: FragmentProfileOverviewBindingImpl

    private val disposer = CompositeDisposable()

    override fun onAttach( context: Context ) {
        (context.applicationContext as ReportMidApp).comp.inject(this)
        super.onAttach(context)
        if( viewModel.activeSummonerModel.value == null ) {
            viewModel.initialize( requireArguments().getParcelable( ARG_PUUID_AND_REG )?: throw IllegalArgumentException( "Missing PUUID argument" ) )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_profile_overview, container, false )
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        for( i in 0 until TOP_MASTERIES_NUM ) {
            val masteryGroup = layoutInflater.inflate(R.layout.champion_mastery, binding.root.grpOtherChampMasteries, false )
            binding.root.grpOtherChampMasteries.addView( masteryGroup )
        }

        return binding.root
    }

    override fun onViewCreated( view: View, savedInstanceState: Bundle? ) {

        super.onViewCreated(view, savedInstanceState)

        viewModel.activeSummonerModel.observe(viewLifecycleOwner ) { summoner ->
            disposer.add( summoner.icon.observeOn( AndroidSchedulers.mainThread() ).subscribe { icon ->
                binding.root.imgSummonerIcon.setImageDrawable( icon )
            } )
            baseActivity.toolbar.title = summoner.name
        }

        for( i in 0 until TOP_MASTERIES_NUM ) {
            with( viewModel.masteries[ i ] ) {
                shownLive.observe(viewLifecycleOwner ) { fShow ->
                    if( fShow != null ) {
                        binding.root.grpOtherChampMasteries[ i ].visibility = if( fShow ) View.VISIBLE else View.GONE
                    }
                }
                iconLive.observe(viewLifecycleOwner ) { icon ->
                    if( icon != null ) {
                        binding.root.grpOtherChampMasteries[i].findViewWithTag<ImageView>( resources.getString(R.string.fragment_profile_overview_tag_champion_icon) ).setImageDrawable( icon )
                    }
                }
/*
                champNameLive.observe(viewLifecycleOwner ) { name ->
                    if( name != null ) {
                        binding.root.grpOtherChampMasteries[i].findViewWithTag<TextView>( resources.getString(R.string.fragment_profile_overview_tag_champion_name) ).text =
                            name
                    }
                }
*/
                skillsLive.observe(viewLifecycleOwner ) { skills ->
                    if( skills != null ) {
                        with(binding.root.grpOtherChampMasteries[i]) {
                            findViewWithTag<TextView>(resources.getString(R.string.fragment_profile_overview_tag_champion_level)).text = skills.level.toString()
                            findViewWithTag<TextView>(resources.getString(R.string.fragment_profile_overview_tag_champion_points)).text = formatPoints( skills.points )
                        }
                    }
                }
            }

            with( binding.root.bottomNavigation ) {
                setOnNavigationItemSelectedListener{ menuItem -> navigateToSibling( menuItem ) }
                selectedItemId = R.id.profileOverviewFragment
            }
        }
    }

    private fun navigateToSibling(item: MenuItem ): Boolean {
        val navOptions = NavOptions.Builder().setLaunchSingleTop( true ).setPopUpTo(R.id.profileOverviewFragment, true ).build()
        return when( item.itemId ) {
            R.id.profileOverviewFragment -> true
            R.id.matchupFragment -> {
                val sum = viewModel.activeSummonerModel.value!!
                val action = ProfileOverviewFragmentDirections.actionProfileOverviewFragmentToMatchupFragment(PuuidAndRegion(sum.puuid,sum.region))
                findNavController().navigate( action, navOptions )
                true
            }
            R.id.matchHistoryFragment -> {
                val sum = viewModel.activeSummonerModel.value!!
                val action = ProfileOverviewFragmentDirections.actionProfileOverviewFragmentToMatchHistoryFragment(PuuidAndRegion(sum.puuid,sum.region))
                findNavController().navigate( action, navOptions )
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        disposer.dispose()
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun newInstance( puuid: String ) = ProfileOverviewFragment().apply {
            arguments = Bundle( 1 ).apply { putString( ARG_PUUID_AND_REG, puuid ) }
        }
    }
}
