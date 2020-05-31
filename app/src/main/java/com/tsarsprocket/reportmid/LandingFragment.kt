package com.tsarsprocket.reportmid

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.databinding.FragmentInitialEnterBinding
import com.tsarsprocket.reportmid.databinding.FragmentLandingBindingImpl
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LandingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LandingFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<LandingViewModel> { viewModelFactory }

    override fun onAttach( context: Context ) {

        ( context.applicationContext as ReportMidApp ).comp.inject( this )

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentLandingBindingImpl>( inflater, R.layout.fragment_landing, container, false )
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        viewModel.hasVerifiedNameState.observe( viewLifecycleOwner, Observer { isVerified ->
            if( !isVerified ) navController.navigate( R.id.initialEnterFragment )
        } )
    }

    companion object {
        @JvmStatic
        fun newInstance() = LandingFragment()
    }
}
