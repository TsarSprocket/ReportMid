package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.BaseFragment
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.databinding.FragmentDrawerBinding
import com.tsarsprocket.reportmid.viewmodel.DrawerViewModel
import kotlinx.android.synthetic.main.fragment_drawer.view.*
import kotlinx.android.synthetic.main.layout_my_summoner_line.view.*
import javax.inject.Inject

class DrawerFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<DrawerViewModel> { viewModelFactory }

    lateinit var binding: FragmentDrawerBinding

    override fun onAttach(context: Context) {
        (context.applicationContext as ReportMidApp).comp.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_drawer, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.eventHandler = EventHandler()

        viewModel.mySummonersInSelectedRegion.observe({ lifecycle }) { updateMySummoners(it) }

        return binding.root
    }

    fun updateMySummoners(listOfMarkedSummoners: List<Triple<Bitmap, String, Boolean>>) {
        val group = binding.root.llMySummoners
        group.removeAllViews()
        listOfMarkedSummoners.forEach { (icon, name, isSelected) ->
            val view = layoutInflater.inflate(R.layout.layout_my_summoner_line, group, false) as ConstraintLayout
            view.iconSummoner.setImageBitmap(icon)
            view.txtSummonerName.text = name
            view.cbSelected.isChecked = isSelected
            group.addView(view)
        }
    }

    fun goManageMySummoners() {
        baseActivity.closeDrawers()
        val action = DrawerFragmentDirections.actionGlobalManageMySummonersFragment()
        findNavController().navigate( action )
    }

    //  Classes  //////////////////////////////////////////////////////////////

    inner class EventHandler {

        fun manageMySummoners(view: View) {
            goManageMySummoners()
        }
    }

    companion object {
        fun newInstance() = DrawerFragment()
    }

}