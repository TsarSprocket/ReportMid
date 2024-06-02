package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsarsprocket.reportmid.ARG_MY_ACC_ID
import com.tsarsprocket.reportmid.BaseFragment
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.RESULT_PUUID_AND_REG
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.baseApi.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.databinding.FragmentManageFriendsBinding
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.summonerApi.model.MyAccount
import com.tsarsprocket.reportmid.summonerApi.model.Summoner
import com.tsarsprocket.reportmid.tools.OneTimeObserver
import com.tsarsprocket.reportmid.tools.getRoundedCroppedDrawable
import com.tsarsprocket.reportmid.tools.removeNavigationReturnedValue
import com.tsarsprocket.reportmid.tools.removePermVar
import com.tsarsprocket.reportmid.tools.setPermVar
import com.tsarsprocket.reportmid.viewmodel.MainActivityViewModel
import com.tsarsprocket.reportmid.viewmodel.ManageFriendsViewModel
import java.security.InvalidParameterException
import javax.inject.Inject

const val VAR_SELECTED_SUMMONER_PUUID_AND_REGION = "VAR_SELECTED_SUMMONER_PUUID_AND_REGION"

class ManageFriendsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<ManageFriendsViewModel> { viewModelFactory }

    private val activityViewModel by activityViewModels<MainActivityViewModel> { viewModelFactory }

    private lateinit var bindings: FragmentManageFriendsBinding

    private val accountSelectorAdapter = AccountSelectorAdapter()
    private val friendsAdapter = FriendsAdapter()

    //  Methods  //////////////////////////////////////////////////////////////

    override fun onAttach(context: Context) {
        (context.applicationContext as ReportMidApp).comp.inject(this)
        super.onAttach(context)
        viewModel.myAccsAndSumsLive.observe(this) { myAccAndPosList ->
            val myAccInitId =
                arguments?.getLong(ARG_MY_ACC_ID) ?: throw InvalidParameterException("${this.javaClass.kotlin.simpleName} requires ARG_MY_ACC_ID parameter")
            viewModel.init(myAccAndPosList.indexOfFirst { it.first.id == myAccInitId })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindings = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_friends, container, false)
        bindings.lifecycleOwner = this
        bindings.viewModel = viewModel
        bindings.eventHandler = EventHandler()

        bindings.accSelector.adapter = accountSelectorAdapter
        viewModel.myAccsAndSumsLive.observe(viewLifecycleOwner) { accountSelectorAdapter.accList = it }

        with (bindings.friendsList) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = friendsAdapter
        }

        viewModel.friendSummonersLive.observe(viewLifecycleOwner) {
            friendsAdapter.lst = it
        }

        viewModel.selectedSummonerLive.observe(viewLifecycleOwner) { sum ->
            baseActivity.toolbar.title = getString(R.string.fragment_manage_my_friends_title).format(sum.name)
        }

        activityViewModel.selectedMenuItem.observe(viewLifecycleOwner) { itemId ->
            when(itemId) {
                R.id.miManageMyFriendsDelete -> { deleteSelectedFriends() }
            }
        }

        activityViewModel.menuRefreshed.observe(viewLifecycleOwner) {
            baseActivity.toolbar.menu.findItem(R.id.miManageMyFriendsDelete).isEnabled = viewModel.checkedItemsLive.value?.isNotEmpty() ?: false
        }

        viewModel.checkedItemsLive.observe(viewLifecycleOwner) { checkedSet ->
            baseActivity.toolbar.menu.findItem(R.id.miManageMyFriendsDelete).isEnabled = checkedSet.isNotEmpty()
        }

        createFriendIfNeeded()

        return bindings.root
    }

    private fun goAddFriend() {
        object : OneTimeObserver<Summoner>() {
            override fun onOneTimeChanged(sum: Summoner) {
                setPermVar(VAR_SELECTED_SUMMONER_PUUID_AND_REGION, sum.puuidAndRegion)
                val action = ManageFriendsFragmentDirections.actionManageFriendsFragmentToAddSummonerGraph(sum.region.tag)
                findNavController().navigate(action)
            }
        }.observeOn(viewModel.selectedSummonerLive, viewLifecycleOwner)
    }

    private fun createFriendIfNeeded() {
        val puuidAndRegion = removeNavigationReturnedValue<PuuidAndRegion>(RESULT_PUUID_AND_REG)

        if (puuidAndRegion != null) {
            viewModel.createFriend(puuidAndRegion, removePermVar(VAR_SELECTED_SUMMONER_PUUID_AND_REGION)!!)
        }
    }

    private fun deleteSelectedFriends() {
        viewModel.deleteSelectedFriends()
    }

    //  Static  ///////////////////////////////////////////////////////////////

    companion object {
        fun newInstance(myAccId: Long) = ManageFriendsFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_MY_ACC_ID, myAccId)
            }
        }
    }

    //  Classes  //////////////////////////////////////////////////////////////

    inner class EventHandler {
        fun addFriend(view: View) { goAddFriend() }
    }

    /**************************************************************************
     * Adapter class for my summoner spinner
     */
    inner class AccountSelectorAdapter: BaseAdapter() {

        var accList: List<Triple<MyAccount, Summoner, Drawable>> = listOf()
            set(lst) {
                field = lst
                notifyDataSetChanged()
            }

        override fun getCount(): Int = accList.size

        override fun getItem(position: Int): Triple<MyAccount, Summoner, Drawable> = accList[position]

        override fun getItemId(position: Int): Long = if (position<accList.size) accList[position].first.id else -1

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val item = getItem(position)
            val view = (convertView?: layoutInflater.inflate(R.layout.view_summoner_selector_round_icon,parent,false)) as ImageView

            return view.apply {
                setImageDrawable(getRoundedCroppedDrawable(item.third))
            }
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val item = getItem(position)
            val view = (convertView?: layoutInflater.inflate(R.layout.list_item_summoner,parent,false)) as ConstraintLayout

            return view.apply {
                findViewById<ImageView>(R.id.imgProfileIcon).setImageDrawable(item.third)
                findViewById<TextView>(R.id.txtSummonerName).text = item.second.name
                findViewById<TextView>(R.id.txtRegion).text = item.second.region.title
            }
        }
    }

    /**************************************************************************
     * Adapter class for the list of friends
     */
    inner class FriendsAdapter: RecyclerView.Adapter<CardViewHolderWithDisposer>() {

        var lst: List<ManageFriendsViewModel.FriendListItem> = listOf()
            set(v) {
                adjustCheckedSummoners(field,v)
                field = v.sortedWith { a, b -> a.sum.name.compareTo(b.sum.name) }
                notifyDataSetChanged()
                updateCheckedLive()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolderWithDisposer =
            CardViewHolderWithDisposer(LayoutInflater.from(parent.context).inflate(R.layout.card_my_managed_summoner,parent,false) as CardView)

        override fun getItemCount() = lst.size

        override fun onBindViewHolder(holder: CardViewHolderWithDisposer, position: Int) {
            val item = if (position in lst.indices) lst[position] else null
            with(holder.cardView) {
                findViewById<ImageView>(R.id.imgProfileIcon).apply { if (item != null) setImageDrawable(item.icon) else setImageResource(R.drawable.champion_icon_placegolder) }
                findViewById<TextView>(R.id.txtSummonerName).text = item?.sum?.name ?: "N/A"
                findViewById<TextView>(R.id.txtRegion).text = item?.sum?.region?.title ?: "N/A"
                with(findViewById<CheckBox>(R.id.cbSelected)) {
                    isChecked = item?.isChecked ?: false
                    setOnCheckedChangeListener{ _, isChckd ->
                        item?.apply { isChecked = isChckd }
                        updateCheckedLive()
                    }
                }
            }
        }

        private fun adjustCheckedSummoners(old: List<ManageFriendsViewModel.FriendListItem>, new: List<ManageFriendsViewModel.FriendListItem>) {
            if (old != new) {
                val hm = new.map { it.sum to it }.toMap()
                old.forEach { hm[ it.sum ]?.isChecked = it.isChecked }
            }
        }

        private fun updateCheckedLive() {
            viewModel.checkedItemsLive.value = lst.withIndex().filter { item -> item.value.isChecked }.toSet()
        }
    }
}