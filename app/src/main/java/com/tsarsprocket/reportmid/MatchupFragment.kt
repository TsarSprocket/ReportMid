package com.tsarsprocket.reportmid

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
import com.tsarsprocket.reportmid.databinding.FragmentMatchupBinding
import com.tsarsprocket.reportmid.model.Repository
import javax.inject.Inject

class MatchupFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MatchupViewModel> { viewModelFactory }

    private lateinit var binding: FragmentMatchupBinding

    override fun onAttach(context: Context) {
        ( context.applicationContext as ReportMidApp ).comp.inject( this )
        super.onAttach(context)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_matchup, container, false )
        binding.viewModel = viewModel

        TODO()

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = MatchupFragment()
    }
}

class MatchupViewModel @Inject constructor( private val repository: Repository ): ViewModel() {

}