package com.tsarsprocket.reportmid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tsarsprocket.reportmid.databinding.FragmentMatchHistoryBinding
import com.tsarsprocket.reportmid.presentation.MatchResultPreviewData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_landing.view.*
import javax.inject.Inject

class MatchHistoryFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by activityViewModels<LandingViewModel> { viewModelFactory }

    private lateinit var binding: FragmentMatchHistoryBinding

    override fun onAttach( context: Context ) {
        ( context.applicationContext as ReportMidApp ).comp.inject( this )
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_match_history, container, false )
        binding.viewModel = viewModel

        with( binding.matchHistoryView ) {
            setHasFixedSize( true )
            layoutManager = LinearLayoutManager( context )
            val disposable = viewModel.activeSummonerModel.value?.matchHistory?.observeOn( AndroidSchedulers.mainThread() )?.subscribe { matchHistory ->
                adapter = MatchHistoryAdapter( object: MatchHistoryAdapter.IHistoryDataProvider {
                    override fun getCount(): Int = matchHistory.size
                    override fun getMatchData( i: Int ): Observable<MatchResultPreviewData> {
                        return viewModel.fetchMatchPreviewInfo( i )
                    }
                } )

                binding.matchHistoryView.visibility = View.VISIBLE
                binding.progressLoading.visibility = View.GONE
            }
            if( disposable != null ) viewModel.allDisposables.add( disposable )
        }

        requireActivity().findViewById<Toolbar>( R.id.toolbar ).title = getString( R.string.fragment_match_history_title_template ).format( viewModel.activeSummonerModel.value?.name )

        with( binding.root.bottomNavigation.menu ) {
            for( i in 0 until size() ) with( get( i ) ) { if( id != R.id.matchHistoryFragment ) isEnabled = true }
        }
        with( binding.root.bottomNavigation ) {
            setOnNavigationItemSelectedListener{ menuItem -> navigateToSibling( menuItem ) }
            selectedItemId = R.id.matchHistoryFragment
        }

        return binding.root
    }

    fun navigateToSibling( item: MenuItem): Boolean {
        val navOptions = NavOptions.Builder().setLaunchSingleTop( true ).setPopUpTo( R.id.matchHistoryFragment, true ).build()
        when( item.itemId ) {
            R.id.landingFragment -> {
                val action = MatchHistoryFragmentDirections.actionMatchHistoryFragmentToLandingFragment()
                findNavController().navigate( action, navOptions )
                return true
            }
            R.id.matchupFragment -> {
                val action = MatchHistoryFragmentDirections.actionMatchHistoryFragmentToMatchupFragment( viewModel.activeSummonerModel.value!!.puuid )
                findNavController().navigate( action, navOptions )
                return true
            }
            R.id.matchHistoryFragment -> return true
            else -> return false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MatchHistoryFragment()
    }
}

