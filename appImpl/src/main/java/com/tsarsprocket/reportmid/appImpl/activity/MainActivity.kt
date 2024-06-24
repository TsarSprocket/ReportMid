package com.tsarsprocket.reportmid.appImpl.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tsarsprocket.reportmid.appImpl.application.ReportMidApplication
import com.tsarsprocket.reportmid.baseApi.di.BaseApi
import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment
import javax.inject.Inject

internal class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var baseApi: BaseApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as ReportMidApplication).applicationComponent.inject(this)

        supportFragmentManager.fragmentFactory = baseApi.getFragmentFactory()

        supportFragmentManager.beginTransaction()
            .add(0, ViewStateFragment::class.java, null, null)
            .commit()
    }
}
