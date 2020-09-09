package com.tsarsprocket.reportmid.controller

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.tsarsprocket.reportmid.BaseActivity
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )

        binding = DataBindingUtil.setContentView( this, R.layout.activity_main )
        binding.setLifecycleOwner { this.lifecycle }

        val navController = findNavController( R.id.nav_host_fragment )
        navController.setGraph( R.navigation.nav_graph, intent.extras )

        binding.toolbar.setNavigationOnClickListener { binding.drawerLayout.openDrawer( GravityCompat.START ) }
/*
        val appBarConfiguration = AppBarConfiguration( navController.graph, drawerLayout = binding.root as DrawerLayout )
        binding.toolbar.setupWithNavController( navController, appBarConfiguration )
*/
    }
}
