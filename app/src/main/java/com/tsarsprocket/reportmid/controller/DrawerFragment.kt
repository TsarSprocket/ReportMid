package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.BaseFragment
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.databinding.FragmentDrawerBinding
import com.tsarsprocket.reportmid.model.RegionModel
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.viewmodel.DrawerViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_drawer.view.*
import kotlinx.android.synthetic.main.layout_my_summoner_line.view.*
import javax.inject.Inject

class DrawerFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<DrawerViewModel> { viewModelFactory }

    private lateinit var binding: FragmentDrawerBinding

    override fun onAttach(context: Context) {
        (context.applicationContext as ReportMidApp).comp.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_drawer, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.eventHandler = EventHandler()

        binding.regionSelector.adapter = RegionAdapter(viewModel.currentRegionsLive)

        viewModel.mySummonersInSelectedRegionLive.observe(viewLifecycleOwner) { updateMySummoners(it) }

        return binding.root
    }

    fun updateMySummoners(listOfMarkedSummoners: List<Triple<Bitmap,SummonerModel,Boolean>>) {
        val group = binding.root.llMySummoners
        group.removeAllViews()
        listOfMarkedSummoners.forEach { (icon, sum, isSelected) ->
            val view = layoutInflater.inflate(R.layout.layout_my_summoner_line, group, false) as ConstraintLayout
            view.iconSummoner.setImageBitmap(icon)
            view.txtSummonerName.text = sum.name
            view.cbSelected.isChecked = isSelected
            view.cbSelected.setOnClickListener { view -> viewModel.activateAcc(sum,view,group) }
            view.setOnClickListener { goSeeProfile(sum) }
            group.addView(view)
        }
    }

    fun goManageMySummoners() {
        baseActivity.closeDrawers()
        val action = DrawerFragmentDirections.actionGlobalManageMySummonersFragment()
        findNavController().navigate(action)
    }

    private fun goManageFriends() {
        baseActivity.closeDrawers()
        val action = DrawerFragmentDirections.actionGlobalManageFriendsFragment(TODO())
    }

    private fun goSeeProfile(sum: SummonerModel) {
        baseActivity.closeDrawers()
        val action = DrawerFragmentDirections.actionGlobalProfileOverviewFragment(sum.puuidAndRegion)
        findNavController().navigate(action)
    }

    //  Classes  //////////////////////////////////////////////////////////////

    inner class EventHandler {

        fun manageMySummoners(view: View) {
            goManageMySummoners()
        }

        fun manageFriends(view: View) {
            goManageFriends()
        }
    }

    inner class RegionAdapter(private val regionsLive: LiveData<List<RegionModel>>): BaseAdapter() {

        private var list: List<RegionModel> = listOf()

        init {
            regionsLive.observe( viewLifecycleOwner ) { setItems(it) }
        }

        fun setItems(newList: List<RegionModel>) {
            list = newList
            notifyDataSetChanged()
        }

        override fun getCount(): Int = list.size

        override fun getItem(position: Int): Any = list[position]

        override fun getItemId(position: Int): Long = if (position < list.size) list[position].ordinal.toLong() else -1L

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View =
            ((convertView ?: layoutInflater.inflate(android.R.layout.simple_spinner_item, parent, false)) as TextView)
                .apply {
                    text = list[position].title
                }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View =
            ((convertView ?: layoutInflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)) as TextView)
                .apply {
                    text = list[position].title
                }
    }

    companion object {
        fun newInstance() = DrawerFragment()
    }
}