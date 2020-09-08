package com.tsarsprocket.reportmid

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tsarsprocket.reportmid.databinding.FragmentDrawerBindingImpl
import javax.inject.Inject

class DrawerFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<DrawerViewModel> { viewModelFactory }

    lateinit var binding: FragmentDrawerBindingImpl

    override fun onAttach(context: Context) {
        (context.applicationContext as ReportMidApp).comp.inject( this )
        super.onAttach(context)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_drawer, container, false )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    companion object {
        fun newInstance() = DrawerFragment()
    }

}