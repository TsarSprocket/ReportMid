package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tsarsprocket.reportmid.ARG_MY_ACC_ID
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.RESULT_PUUID_AND_REG
import com.tsarsprocket.reportmid.ReportMidApp
import com.tsarsprocket.reportmid.databinding.FragmentManageFriendsBinding
import com.tsarsprocket.reportmid.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.model.state.MyAccountModel
import com.tsarsprocket.reportmid.tools.*
import com.tsarsprocket.reportmid.viewmodel.ManageFriendsViewModel
import java.security.InvalidParameterException
import javax.inject.Inject

const val VAR_SELECTED_SUMMONER_PUUID_AND_REGION = "VAR_SELECTED_SUMMONER_PUUID_AND_REGION"

class ManageFriendsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ManageFriendsViewModel> { viewModelFactory }

    private lateinit var bindings: FragmentManageFriendsBinding

    private val accountSelectorAdapter = AccountSelectorAdapter()

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

        createFriendIfNeeded()

        return bindings.root
    }

    private fun goAddFriend() {
        object: OneTimeObserver<SummonerModel>() {
            override fun onOneTimeChanged(sum: SummonerModel) {
                setPermVar(VAR_SELECTED_SUMMONER_PUUID_AND_REGION, sum.puuidAndRegion)
                val action = ManageFriendsFragmentDirections.actionManageFriendsFragmentToAddSummonerGraph(sum.region.tag)
                findNavController().navigate(action)
            }
        }.observeOn(viewModel.selectedSummonerLive, this)
    }

    private fun createFriendIfNeeded() {
        val puuidAndRegion = removeNavigationReturnedValue<PuuidAndRegion>(RESULT_PUUID_AND_REG)

        if (puuidAndRegion != null) {
            viewModel.createFriend(puuidAndRegion, removePermVar(VAR_SELECTED_SUMMONER_PUUID_AND_REGION)!!)
        }
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

    inner class AccountSelectorAdapter(): BaseAdapter() {

        var accList: List<Triple<MyAccountModel,SummonerModel,Bitmap>> = listOf()
            set(lst) {
                field = lst
                notifyDataSetChanged()
            }

        override fun getCount(): Int = accList.size

        override fun getItem(position: Int): Triple<MyAccountModel,SummonerModel,Bitmap> = accList[position]

        override fun getItemId(position: Int): Long = if (position<accList.size) accList[position].first.id else -1

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val item = getItem(position)
            val view = (convertView?: layoutInflater.inflate(R.layout.view_summoner_selector_round_icon,parent,false)) as ImageView

            return view.apply {
                setImageBitmap(getRoundedCroppedBitmap(item.third, 6))
            }
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val item = getItem(position)
            val view = (convertView?: layoutInflater.inflate(R.layout.list_item_summoner,parent,false)) as ConstraintLayout

            return view.apply {
                findViewById<ImageView>(R.id.imgProfileIcon).setImageBitmap(item.third)
                findViewById<TextView>(R.id.txtSummonerName).text = item.second.name
                findViewById<TextView>(R.id.txtRegion).text = item.second.region.tag
            }
        }
    }
}