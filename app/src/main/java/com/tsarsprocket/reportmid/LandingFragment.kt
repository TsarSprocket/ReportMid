package com.tsarsprocket.reportmid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.databinding.FragmentLandingBindingImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_landing.view.*
import javax.inject.Inject

class LandingFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by activityViewModels<LandingViewModel> { viewModelFactory }

    lateinit var binding: FragmentLandingBindingImpl

    lateinit var masteryGroups: List<ViewGroup>

    val fragmentDisposables = CompositeDisposable()

    override fun onAttach(context: Context) {

        (context.applicationContext as ReportMidApp).comp.inject(this)

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_landing, container, false )
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        for( i in 0 until TOP_MASTERIES_NUM ) {
            val masteryGroup = layoutInflater.inflate( R.layout.champion_mastery, binding.root.grpOtherChampMasteries, false )
            binding.root.grpOtherChampMasteries.addView( masteryGroup )
        }

        return binding.root
    }

    override fun onViewCreated( view: View, savedInstanceState: Bundle? ) {

        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        viewModel.state.observe( viewLifecycleOwner, Observer { state ->
            when( state ) {
                LandingViewModel.Status.UNVERIFIED -> navController.navigate( NavGraphDirections.actionGlobalInitialEnterFragment() )
            }
        } )

        viewModel.activeSummonerModel.observe( { this.viewLifecycleOwner.lifecycle } ) { summoner ->
            fragmentDisposables.add( summoner.icon.observeOn( AndroidSchedulers.mainThread() ).subscribe { bitmap ->
                binding.root.imgSummonerIcon.setImageBitmap( bitmap )
            } )
            requireActivity().findViewById<Toolbar>( R.id.toolbar ).title = getString( R.string.fragment_landing_title_template ).format( summoner.name )
        }

        for( i in 0 until TOP_MASTERIES_NUM ) {
            with( viewModel.masteries[ i ] ) {
                shownLive.observe( { lifecycle } ) { fShow ->
                    if( fShow != null ) {
                        binding.root.grpOtherChampMasteries[ i ].visibility = if( fShow ) View.VISIBLE else View.GONE
                    }
                }
                bitmapLive.observe( { lifecycle } ) { bitmap ->
                    if( bitmap != null ) {
                        binding.root.grpOtherChampMasteries[i].findViewWithTag<ImageView>( resources.getString( R.string.fragment_landing_tag_champion_icon ) )
                            .setImageBitmap( bitmap )
                    }
                }
                champNameLive.observe( { lifecycle } ) { name ->
                    if( name != null ) {
                        binding.root.grpOtherChampMasteries[i].findViewWithTag<TextView>( resources.getString( R.string.fragment_landing_tag_champion_name ) ).text =
                            name
                    }
                }
                skillsLive.observe( { lifecycle } ) { skills ->
                    if( skills != null ) {
                        with(binding.root.grpOtherChampMasteries[i]) {
                            findViewWithTag<TextView>(resources.getString(R.string.fragment_landing_tag_champion_level)).text = skills.level.toString()
                            findViewWithTag<TextView>(resources.getString(R.string.fragment_landing_tag_champion_points)).text = skills.points.toString()
                        }
                    }
                }
            }

            with( binding.root.bottomNavigation ) {
                setOnNavigationItemSelectedListener{ menuItem -> navigateToSibling( menuItem ) }
                selectedItemId = R.id.landingFragment
            }
        }

/*
        for( i: Int in 0 until TOP_MASTERIES_NUM ) {
            viewModel.championImages[ i ].observe( viewLifecycleOwner, Observer { b ->
                with( masteryGroups[ i ] ) {
                    with( findViewWithTag<ImageView>( getString( R.string.fragment_landing_tag_champion_icon ) ) ) {
                        if( !b.isEmpty.blockingGet() ) {
                            setImageBitmap( b.blockingGet() )
                            visibility = View.VISIBLE
                        } else {
                            visibility = View.GONE
                        }
                    }

                    fragmentDisposables.add( viewModel.activeSummonerModel.value!!.masteries
                        .flatMap { masteryList -> masteryList[ i ] }
                        .flatMap { championMastery -> championMastery.champion }
                        .observeOn( AndroidSchedulers.mainThread() )
                        .subscribe { champion ->
                            findViewWithTag<TextView>( getString( R.string.fragment_landing_tag_champion_name ) ).text = champion.name
                        } )
                }
            } )
        }
*/
    }

    fun navigateToSibling( item: MenuItem ): Boolean {
        val navOptions = NavOptions.Builder().setLaunchSingleTop( true ).setPopUpTo( R.id.landingFragment, true ).build()
        return when( item.itemId ) {
            R.id.landingFragment -> true
            R.id.matchupFragment -> {
                val action = LandingFragmentDirections.actionLandingFragmentToMatchupFragment( viewModel.activeSummonerModel.value!!.puuid )
                findNavController().navigate( action, navOptions )
                true
            }
            R.id.matchHistoryFragment -> {
                val action = LandingFragmentDirections.actionLandingFragmentToMatchHistoryFragment( viewModel.activeSummonerModel.value!!.puuid )
                findNavController().navigate( action, navOptions )
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        fragmentDisposables.dispose()
        super.onDestroy()
    }

/*
    private fun refreshScreen( summoner: SummonerModel ) {
        val masteryViewGroup = binding.root.findViewById<ViewGroup>( R.id.grpOtherChampMasteries )

        masteryViewGroup.children.forEach { child -> child.visibility = View.GONE }

        fragmentDisposables.add( summoner.masteries.observeOn( AndroidSchedulers.mainThread() ).subscribe { masteries ->
            val extraSumSize = if( masteries.size < TOP_MASTERIES_NUM ) masteries.size else TOP_MASTERIES_NUM

            masteryGroups = List<ViewGroup>( extraSumSize ) { i ->
                val mastery = masteries[i]
                val viewGroup = layoutInflater.inflate( R.layout.champion_mastery, masteryViewGroup, false )
                    .also { masteryViewGroup.addView( it ) } as ViewGroup

                fragmentDisposables.add( mastery.observeOn( AndroidSchedulers.mainThread() ).subscribe { m ->
                    viewGroup.findViewById<TextView>(R.id.txtChampLevel).text = m.level.toString()
                    viewGroup.findViewById<TextView>(R.id.txtChampPoints).text = m.points.toString()
                } )

                return@List viewGroup
            }


            binding.invalidateAll()
        } )
    }
*/

    companion object {
        @JvmStatic
        fun newInstance() = LandingFragment()
    }
}
