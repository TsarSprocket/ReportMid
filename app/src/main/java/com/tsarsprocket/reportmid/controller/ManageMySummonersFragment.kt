package com.tsarsprocket.reportmid.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsarsprocket.reportmid.*
import com.tsarsprocket.reportmid.databinding.FragmentManageMySummonersBinding
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.tools.OneTimeObserver
import com.tsarsprocket.reportmid.tools.getNavigationReturnedValue
import com.tsarsprocket.reportmid.tools.removeNavigationReturnedValue
import com.tsarsprocket.reportmid.viewmodel.ManageMySummonersViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.card_my_managed_summoner.view.*
import javax.inject.Inject

class ManageMySummonersFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ManageMySummonersViewModel> { viewModelFactory }

    lateinit var binding: FragmentManageMySummonersBinding

    override fun onAttach(context: Context) {
        (context.applicationContext as ReportMidApp).comp.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_my_summoners, container, false)
        binding.lifecycleOwner = this
        binding.handler = EventHandler()

        baseActivity.toolbar.title = getString(R.string.fragment_manage_my_summoners_title)

        val mySummonersAdapter = MySummonersAdapter()
        with(binding.recvMySummoners) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mySummonersAdapter
            viewModel.mySummonersLive.observe({ lifecycle }) { lstSummoners ->
                binding.progressLoading.visibility = View.GONE
                visibility = View.VISIBLE
                mySummonersAdapter.summoners = lstSummoners.toTypedArray()
            }
        }

        return binding.root
    }

    private fun doAddSummoner() {
        object : OneTimeObserver<String>() {
            override fun onOneTimeChanged(v:String) {
                removeNavigationReturnedValue<String>(RESULT_PUUID)
                viewModel.addMySummoner(v)
            }
        }.observeOn(getNavigationReturnedValue<String>(RESULT_PUUID),this)

/*
        val navigationResultLive = getNavigationResult<String>(RESULT_PUUID)
        val observer = object : Observer<String> {
            override fun onChanged(puuid: String) {
                viewModel.addMySummoner(puuid)
                navigationResultLive.removeObserver(this)
            }
        }
        navigationResultLive.observe(this, observer)
*/
        findNavController().navigate(ManageMySummonersFragmentDirections.actionManageMySummonersFragmentToAddSummonerGraph(null))
    }

    companion object {
        fun newInstance() = ManageMySummonersFragment()
    }

    //  Classes  //////////////////////////////////////////////////////////////

    class MySummonersAdapter : RecyclerView.Adapter<CardViewHolderWithDisposer>() {

        var summoners: Array<SummonerModel> = arrayOf()
            set(v: Array<SummonerModel>) {
                field = v
                notifyDataSetChanged()
            }

        val checkedItems = HashSet<SummonerModel>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            CardViewHolderWithDisposer(LayoutInflater.from(parent.context).inflate(R.layout.card_my_managed_summoner, parent, false) as CardView)

        override fun onBindViewHolder(holder: CardViewHolderWithDisposer, position: Int) {
            holder.disposer.clear()
            val summoner = summoners[position]
            with(holder.cardView) {
                cbSelected.setOnCheckedChangeListener { _, isChecked -> if (isChecked) checkedItems.add(summoner) else checkedItems.remove(summoner) }
                holder.disposer.add(summoner.icon.observeOn(AndroidSchedulers.mainThread()).subscribe { bmp -> imgProfileIcon.setImageBitmap(bmp) })
                txtSummonerName.text = summoner.name
                txtRegion.text = summoner.region.tag
            }
        }

        override fun getItemCount() = summoners.size
    }

    inner class EventHandler {
        fun onAddSummonerClick(view: View) {
            doAddSummoner()
        }
    }
}