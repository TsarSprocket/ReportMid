package com.tsarsprocket.reportmid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.tsarsprocket.reportmid.databinding.ActivityLandingBinding

class LandingActivity : BaseActivity() {

    lateinit var binding: ActivityLandingBinding

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )

        binding = DataBindingUtil.setContentView( this, R.layout.activity_landing )
        binding.lifecycleOwner = this
    }
}