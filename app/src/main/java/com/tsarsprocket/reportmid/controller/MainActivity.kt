package com.tsarsprocket.reportmid.controller

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.tsarsprocket.reportmid.BaseActivity
import com.tsarsprocket.reportmid.MENU_ITEM_NONE
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.app.view.ReportMidFragmentFactory
import com.tsarsprocket.reportmid.base.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.databinding.ActivityMainBinding
import com.tsarsprocket.reportmid.viewmodel.MainActivityViewModel
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var fragmentFactory: ReportMidFragmentFactory

    private val viewModel by viewModels<MainActivityViewModel> { viewModelFactory }

    lateinit var binding: ActivityMainBinding

    override val toolbar: Toolbar get() = binding.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.fragmentFactory = fragmentFactory

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        val navController = findNavController(R.id.nav_host_fragment)
        navController.setGraph(R.navigation.nav_graph, intent.extras)

        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.START) }

        toolbar.setOnMenuItemClickListener { menuItem ->
            viewModel.selectedMenuItem.value = menuItem.itemId
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ -> updateToolbarMenu(destination) }

        viewModel.updateToolbarMenuForDestination.observe(this) { destId ->
            findNavController(R.id.nav_host_fragment).currentDestination?.let { currentDestination ->
                if (currentDestination.id == destId) updateToolbarMenu(currentDestination)
            }
        }
/*
        val appBarConfiguration = AppBarConfiguration( navController.graph, drawerLayout = binding.root as DrawerLayout )
        binding.toolbar.setupWithNavController( navController, appBarConfiguration )
*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val destination = findNavController(R.id.nav_host_fragment).currentDestination
        if (menu != null && destination != null) {
            updateMenuForDestination(menu, destination)
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val destination = findNavController(R.id.nav_host_fragment).currentDestination
        val shouldUpdate = destination != null && viewModel.toolbarMenuByDestination[destination.id] != null
        if (shouldUpdate) viewModel.menuRefreshed.value = true
        return shouldUpdate
    }

    private fun updateToolbarMenu(destination: NavDestination) {
        updateMenuForDestination(toolbar.menu, destination)
    }

    private fun updateMenuForDestination(menu: Menu, destination: NavDestination) {
        menu.clear()
        viewModel.toolbarMenuByDestination[destination.id]?.let { menuId ->
            viewModel.selectedMenuItem.value = MENU_ITEM_NONE
            menuInflater.inflate(menuId, menu)
        }
    }

    override fun closeDrawers() {
        binding.drawerLayout.closeDrawers()
    }
}
