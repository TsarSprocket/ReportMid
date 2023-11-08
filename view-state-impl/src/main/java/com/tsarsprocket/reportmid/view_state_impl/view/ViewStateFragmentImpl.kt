package com.tsarsprocket.reportmid.view_state_impl.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.tsarsprocket.reportmid.base.viewmodel.ViewModelFactory
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.view_state_api.view.ViewStateFragment
import com.tsarsprocket.reportmid.view_state_api.view_state.ViewState
import com.tsarsprocket.reportmid.view_state_impl.view_model.ViewStateViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class ViewStateFragmentImpl @Inject constructor(
    viewModelFactory: ViewModelFactory,
) : ViewStateFragment() {

    private val viewModel: ViewStateViewModel by viewModels { viewModelFactory }

    private val backPressedCallback = object : OnBackPressedCallback(ENABLE_CALLBACK_ON_START) {

        override fun handleOnBackPressed() {
            viewModel.tryGoBack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stackSize.collect { stackSize ->
                    backPressedCallback.isEnabled = stackSize > 0
                }
            }
        }

        viewModel.viewModelScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewEffects.collect { effect ->
                    viewModel.findEffectHandler(effect)?.handleEffect(effect, this@ViewStateFragmentImpl, viewModel)
                }
            }
        }

        (view as ComposeView).setContent {
            ReportMidTheme {
                VisualizeState(viewModel.viewStates.collectAsState().value)
            }
        }
    }

    @Composable
    private fun VisualizeState(state: ViewState) {
        viewModel.findStateVisualizer(state)?.visualize(state, viewModel)
    }

    companion object {
        const val ENABLE_CALLBACK_ON_START = false
    }
}