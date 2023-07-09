package com.tsarsprocket.reportmid

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    val baseActivity by lazy { requireActivity() as BaseActivity }
}
