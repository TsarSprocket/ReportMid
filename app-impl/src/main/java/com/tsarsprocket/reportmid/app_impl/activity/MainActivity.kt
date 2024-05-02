package com.tsarsprocket.reportmid.app_impl.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tsarsprocket.reportmid.app_impl.di.AppContextProviderModule
import com.tsarsprocket.reportmid.app_impl.di.AppProvisioningModule
import com.tsarsprocket.reportmid.app_impl.di.DaggerAppApiComponent

class MainActivity : AppCompatActivity() {

    init {
        AppProvisioningModule.appApiComponent = DaggerAppApiComponent.factory().create(AppContextProviderModule { applicationContext })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
