package com.tsarsprocket.reportmid.controller

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.tsarsprocket.reportmid.BaseActivity
import com.tsarsprocket.reportmid.R
import com.tsarsprocket.reportmid.databinding.ActivityLandingBinding

class LandingActivity : BaseActivity() {

    lateinit var binding: ActivityLandingBinding

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )

        binding = DataBindingUtil.setContentView( this, R.layout.activity_landing)
        binding.lifecycleOwner = this
    }
}