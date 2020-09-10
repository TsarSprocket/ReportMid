package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tsarsprocket.reportmid.*
import com.tsarsprocket.reportmid.databinding.FragmentAddSummonerBinding
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.viewmodel.AddSummonerViewModel
import kotlinx.android.synthetic.main.fragment_add_summoner.view.*
import java.text.Format
import java.util.*
import javax.inject.Inject

class AddSummonerFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<AddSummonerViewModel> { viewModelFactory }

    lateinit var binding: FragmentAddSummonerBinding

    override fun onAttach(context: Context) {

        ( context.applicationContext as ReportMidApp).comp.inject( this )

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<FragmentAddSummonerBinding>( inflater, R.layout.fragment_add_summoner, container, false )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.fragment = this

        binding.root.findViewById<Spinner>( R.id.spRegion ).onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected( parent: AdapterView<*>? ) {
                    viewModel.selectRegionByOrderNo( -1 )
                }

                override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                    viewModel.selectRegionByOrderNo( position )
                }
            }

        viewModel.showSoftInput.observe( viewLifecycleOwner ) { setSoftInputVisibility( requireContext(), binding.root.edSummonerName, it ) }

        viewModel.showNotFoundNotifier.observe( viewLifecycleOwner ) {
            viewModel.activeSummonerName.observe( viewLifecycleOwner ) { summonerName ->
                Snackbar.make( binding.root, Formatter().format( getString( R.string.snack_summoner_not_found ), summonerName ).toString(), Snackbar.LENGTH_SHORT ).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe( viewLifecycleOwner, { state ->
            when (state) {
                AddSummonerViewModel.Status.VERIFIED -> {
                    val action = AddSummonerFragmentDirections.actionAddSummonerFragmentToConfirmSummonerFragment( viewModel.activeSummonerModel.value!!.puuid )
                    findNavController().navigate( action )
                }
                AddSummonerViewModel.Status.UNVERIFIED -> {
                    Snackbar.make( requireView(), "No summoner fount for name ${viewModel.activeSummonerName}", Snackbar.LENGTH_LONG ).show()
                }
            }
        })
        getNavigationResult<Boolean>( RESULT_CONFIRM ).switchMap { confirmed -> if( confirmed ) viewModel.activeSummonerModel else MutableLiveData() }
            .observe( this.viewLifecycleOwner ) { summoner ->
                setNavigationResult( result = summoner.puuid, key = RESULT_PUUID )
                findNavController().popBackStack()
            }
    }

    fun onValidateInitial( view: View? ) { viewModel.checkSummoner() }

    companion object {
        @JvmStatic
        fun newInstance() = AddSummonerFragment()
    }
}

