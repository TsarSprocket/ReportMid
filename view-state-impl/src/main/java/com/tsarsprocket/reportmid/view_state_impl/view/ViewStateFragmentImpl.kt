package com.tsarsprocket.reportmid.view_state_impl.view

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.tsarsprocket.reportmid.base_api.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.view_state_api.view.ViewStateFragment
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewIntent
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewState
import com.tsarsprocket.reportmid.view_state_impl.viewmodel.ViewStateViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class ViewStateFragmentImpl @Inject constructor(
    viewModelFactory: ViewModelFactory,
) : ViewStateFragment() {

    private val viewModel: ViewStateViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return viewModelFactory.create(modelClass).also { viewModel ->
                    (arguments?.getParcelable<Parcelable>(INITIAL_STATE) as? ViewState)?.let { initialState ->
                        (viewModel as ViewStateViewModel).setInitialState(initialState)
                    }
                    (arguments?.getParcelable<Parcelable>(START_INTENT) as? ViewIntent)?.let { startIntent ->
                        (viewModel as ViewStateViewModel).rootHolder.postIntent(startIntent)
                    }
                }
            }
        }
    }

    private val backPressedCallback = object : OnBackPressedCallback(ENABLE_CALLBACK_ON_START) {

        override fun handleOnBackPressed() {
            viewModel.tryGoBack()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.updateLayoutParams<ViewGroup.LayoutParams> {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            width = ViewGroup.LayoutParams.MATCH_PARENT
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)

        viewModel.viewModelScope.launch {
            viewModel.stackSize.collect { stackSize ->
                backPressedCallback.isEnabled = stackSize > 0
            }
        }

        viewModel.viewModelScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewEffects.collect { effectAction -> effectAction(this@ViewStateFragmentImpl) }
            }
        }

        (view as ComposeView).setContent {
            ReportMidTheme {
                viewModel.rootHolder.Visualize()
            }
        }
    }

    companion object {
        const val ENABLE_CALLBACK_ON_START = false
    }
}