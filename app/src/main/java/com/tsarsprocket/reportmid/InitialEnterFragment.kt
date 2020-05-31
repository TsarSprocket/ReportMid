package com.tsarsprocket.reportmid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tsarsprocket.reportmid.databinding.FragmentInitialEnterBinding
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InitialEnterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InitialEnterFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<LandingViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentInitialEnterBinding>( inflater, R.layout.fragment_initial_enter, container, false )
        binding.viewModel = viewModel

        binding.root.findViewById<Spinner>( R.id.spRegion ).onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    viewModel.selectedRegionTag.value = null
                }

                override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                    viewModel.selectedRegionTag.value = parent?.adapter?.getItem( position ).toString()
                }
            }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = InitialEnterFragment()
    }
}

