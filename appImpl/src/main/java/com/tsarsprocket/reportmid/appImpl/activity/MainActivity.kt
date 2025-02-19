package com.tsarsprocket.reportmid.appImpl.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tsarsprocket.reportmid.appImpl.application.ReportMidApplication
import com.tsarsprocket.reportmid.baseApi.di.BaseApi
import com.tsarsprocket.reportmid.navigationMapApi.di.NavigationMapApi
import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment
import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment.Companion.START_INTENT
import javax.inject.Inject

internal class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var baseApi: BaseApi

    @Inject
    lateinit var navigationMapApi: NavigationMapApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as ReportMidApplication).applicationComponent.inject(this)

        supportFragmentManager.fragmentFactory = baseApi.getFragmentFactory()

        supportFragmentManager.beginTransaction()
            .add(
                0,
                ViewStateFragment::class.java,
                Bundle(1).apply {
                    putParcelable(START_INTENT, navigationMapApi.getStartViewIntentCreator())
                },
                null
            )
            .commit()
    }
}
