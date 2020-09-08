package com.tsarsprocket.reportmid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.tsarsprocket.reportmid.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )

        binding = DataBindingUtil.setContentView( this, R.layout.activity_main )
        binding.setLifecycleOwner { this.lifecycle }

        val navController = findNavController( R.id.nav_host_fragment )
        val appBarConfiguration = AppBarConfiguration( navController.graph, drawerLayout = binding.root as DrawerLayout )
        binding.toolbar.setupWithNavController( navController, appBarConfiguration )
//        binding.bottomNavigation.setupWithNavController( navController )
    }
}
