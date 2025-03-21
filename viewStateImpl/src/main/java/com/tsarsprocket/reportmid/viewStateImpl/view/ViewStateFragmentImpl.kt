package com.tsarsprocket.reportmid.viewStateImpl.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.activity.OnBackPressedCallback
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.tsarsprocket.reportmid.baseApi.viewmodel.ViewModelFactoryProvider
import com.tsarsprocket.reportmid.baseApi.viewmodel.viewModels
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.viewStateApi.view.ViewStateFragment
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateImpl.viewmodel.ViewStateViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class ViewStateFragmentImpl @Inject constructor(
    viewModelFactoryProvider: ViewModelFactoryProvider,
) : ViewStateFragment() {

    override val snackbarHostState = SnackbarHostState()

    private val viewModel: ViewStateViewModel by viewModels(viewModelFactoryProvider)

    private val backPressedCallback = object : OnBackPressedCallback(ENABLE_CALLBACK_ON_START) {

        override fun handleOnBackPressed() {
            viewModel.goBack()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext())
    }

    override fun onStop() {
        viewModel.saveState()
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(view.layoutParams != null) {
            view.updateLayoutParams<LayoutParams> {
                height = MATCH_PARENT
                width = MATCH_PARENT
            }
        } else {
            view.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)

        viewModel.viewModelScope.launch {
            viewModel.stackSize.collect { stackSize ->
                backPressedCallback.isEnabled = stackSize > 0
            }
        }

        viewModel.viewModelScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewEffectActions.collect { effectAction -> effectAction(this@ViewStateFragmentImpl) }
            }
        }

        (view as ComposeView).setContent {
            ReportMidTheme {
                viewModel.Visualize()

                SnackbarHost(
                    hostState = remember { snackbarHostState }
                )
            }
        }
    }

    override fun postIntent(viewIntent: ViewIntent) {
        viewModel.postIntent(viewIntent)
    }

    companion object {
        const val ENABLE_CALLBACK_ON_START = false
    }
}