package com.tsarsprocket.reportmid.controller

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.tsarsprocket.reportmid.BaseActivity
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.databinding.ActivityMainBinding
import com.tsarsprocket.reportmid.viewmodel.MainActivityViewModel
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MainActivityViewModel> { viewModelFactory }

    lateinit var binding: ActivityMainBinding

    override val toolbar: Toolbar get() = binding.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.setLifecycleOwner { this.lifecycle }

        val navController = findNavController(R.id.nav_host_fragment)
        navController.setGraph(R.navigation.nav_graph, intent.extras)

        setSupportActionBar( binding.toolbar )

        binding.toolbar.setNavigationOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.START) }

        toolbar.setOnMenuItemClickListener { menuItem ->
            viewModel.selectedMenuItem.value = menuItem.itemId
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ -> updateToolbarMenu(destination) }

        viewModel.updateToolbarMenuForDestination.observe({ lifecycle }) { destId ->
            findNavController(R.id.nav_host_fragment).currentDestination?.let { currentDestination ->
                if (currentDestination.id == destId) updateToolbarMenu(currentDestination)
            }
        }
/*
        val appBarConfiguration = AppBarConfiguration( navController.graph, drawerLayout = binding.root as DrawerLayout )
        binding.toolbar.setupWithNavController( navController, appBarConfiguration )
*/
    }

    private fun updateToolbarMenu(destination: NavDestination) {
        toolbar.menu.clear()
        viewModel.toolbarMenuByDestination[destination.id]?.let { menuId -> menuInflater.inflate( menuId, toolbar.menu ) }
    }
}
