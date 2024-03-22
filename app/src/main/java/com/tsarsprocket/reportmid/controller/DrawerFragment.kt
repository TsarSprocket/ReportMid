package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.BaseFragment
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.base.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.databinding.FragmentDrawerBinding
import com.tsarsprocket.reportmid.databinding.LayoutMyFriendLineBinding
import com.tsarsprocket.reportmid.databinding.LayoutMySummonerLineBinding
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.summoner_api.model.MyAccount
import com.tsarsprocket.reportmid.summoner_api.model.Summoner
import com.tsarsprocket.reportmid.tools.OneTimeObserver
import com.tsarsprocket.reportmid.tools.Optional
import com.tsarsprocket.reportmid.viewmodel.DrawerViewModel
import javax.inject.Inject

class DrawerFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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

        viewModel.myFriendsLive.observe(viewLifecycleOwner) { updateMyFriends(it) }

        return binding.root
    }

    fun updateMySummoners(listOfMarkedSummoners: List<Triple<Drawable, Summoner, Boolean>>) {
        val group = binding.llMySummoners
        group.removeAllViews()
        listOfMarkedSummoners.forEach { (icon, sum, isSelected) ->
            val binding = LayoutMySummonerLineBinding.inflate(layoutInflater, group, false)
            binding.iconSummoner.setImageDrawable(icon)
            binding.txtSummonerName.text = sum.name
            binding.cbSelected.isChecked = isSelected
            binding.cbSelected.setOnClickListener { view -> viewModel.activateAcc(sum) }
            binding.root.setOnClickListener { goSeeProfile(sum) }
            group.addView(binding.root)
        }
    }

    private fun updateMyFriends(listOfMyFriendData: List<DrawerViewModel.MyFriendData>) {
        val sortedListOfMyFriendData = listOfMyFriendData.sortedWith { a, b -> a.name.compareTo(b.name, true) }

        val group = binding.llMyFriends

        ensureChildren(group,listOfMyFriendData.size) { layoutInflater.inflate(R.layout.layout_my_friend_line, group, false) }

        sortedListOfMyFriendData.withIndex().forEach { indexedValue ->
            val binding = LayoutMyFriendLineBinding.bind(group.getChildAt(indexedValue.index))
            val data = indexedValue.value
            binding.iconSummoner.setImageDrawable(data.icon)
            binding.txtSummonerName.text = data.name
            binding.root.setOnClickListener { goSeeProfile(data.sum) }
        }
    }

    fun goManageMySummoners() {
        baseActivity.closeDrawers()
        val action = DrawerFragmentDirections.actionGlobalManageMySummonersFragment()
        findNavController().navigate(action)
    }

    private fun goManageFriends() {
        baseActivity.closeDrawers()
        object : OneTimeObserver<Optional<MyAccount>>() {
            override fun onOneTimeChanged(optional: Optional<MyAccount>) {
                optional.ifHasValue { myAccModel ->
                    val action = DrawerFragmentDirections.actionGlobalManageFriendsFragment(myAccModel.id)
                    findNavController().navigate(action)
                }
            }
        }.observeOn(viewModel.currentAccountLive,this)
    }

    private fun goSeeProfile(sum: Summoner) {
        baseActivity.closeDrawers()
        val action = DrawerFragmentDirections.actionGlobalProfileOverviewFragment(sum.puuidAndRegion)
        findNavController().navigate(action)
    }

    //  Classes  //////////////////////////////////////////////////////////////

    inner class EventHandler {

        fun manageMySummoners(@Suppress("UNUSED_PARAMETER") view: View) {
            goManageMySummoners()
        }

        fun manageFriends(@Suppress("UNUSED_PARAMETER") view: View) {
            goManageFriends()
        }
    }

    inner class RegionAdapter(private val regionsLive: LiveData<List<Region>>): BaseAdapter() {

        private var list: List<Region> = listOf()

        init {
            regionsLive.observe( viewLifecycleOwner ) { setItems(it) }
        }

        fun setItems(newList: List<Region>) {
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

    //  Util  /////////////////////////////////////////////////////////////////

    fun ensureChildren(grp: ViewGroup, n: Int, viewCreator: () -> View) {
        if (grp.childCount > n) repeat(grp.childCount - n) { grp.removeViewAt(grp.childCount - 1) }
        else if (grp.childCount < n ) repeat(n - grp.childCount) { grp.addView(viewCreator()) }
    }
}