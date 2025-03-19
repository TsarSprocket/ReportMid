package com.tsarsprocket.reportmid.appImpl.activity

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.tsarsprocket.reportmid.appImpl.R
import com.tsarsprocket.reportmid.appImpl.application.ReportMidApplication
import com.tsarsprocket.reportmid.baseApi.di.BaseApi
import com.tsarsprocket.reportmid.navigationMapApi.di.NavigationMapApi
import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment
import javax.inject.Inject

internal class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var baseApi: BaseApi

    @Inject
    lateinit var navigationMapApi: NavigationMapApi

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ReportMidApplication).applicationComponent.inject(this)

        supportFragmentManager.fragmentFactory = baseApi.getFragmentFactory()

        super.onCreate(savedInstanceState)

        setContentView(FragmentContainerView(this).apply {
            id = R.id.main_frame
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        })

        if(savedInstanceState == null) {
            supportFragmentManager.commit {
                add<ViewStateFragment>(R.id.main_frame)
                runOnCommit {
                    (supportFragmentManager.findFragmentById(R.id.main_frame) as ViewStateFragment).postIntent(navigationMapApi.getStartViewIntentCreator())
                }
            }
        }
    }
}
