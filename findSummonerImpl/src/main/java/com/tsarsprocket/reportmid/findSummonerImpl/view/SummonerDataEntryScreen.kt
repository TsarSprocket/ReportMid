package com.tsarsprocket.reportmid.findSummonerImpl.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tsarsprocket.reportmid.findSummonerImpl.R
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.InternalViewIntent.FindAndConfirmSummonerViewIntent
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.InternalViewIntent.GameNameChanged
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.InternalViewIntent.RegionSelected
import com.tsarsprocket.reportmid.findSummonerImpl.viewIntent.InternalViewIntent.TagLineChanged
import com.tsarsprocket.reportmid.findSummonerImpl.viewState.SummonerDataEntryViewState
import com.tsarsprocket.reportmid.lol.model.GameName
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.lol.model.Region.Companion.NONEXISTENT_REGION_ID
import com.tsarsprocket.reportmid.lol.model.TagLine
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.viewStateApi.viewState.PreviewViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewStateHolder
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ViewStateHolder.SummonerDataEntryScreen(state: SummonerDataEntryViewState) {
    Scaffold { pagePadding ->
        Column(
            modifier = Modifier
                .padding(pagePadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var showRegionSelector by remember { mutableStateOf(false) }
            val regionSelectorState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            val scope = rememberCoroutineScope()

            Spacer(modifier = Modifier.weight(0.4f))

            Row(
                modifier = Modifier.fillMaxWidth(0.7f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(0.7f),
                    value = state.gameName.value,
                    singleLine = true,
                    onValueChange = { newValue -> postIntent(GameNameChanged(newValue)) },
                )

                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = stringResource(id = R.string.addAccountImplHash),
                )

                OutlinedTextField(
                    modifier = Modifier.weight(0.3f),
                    value = state.tagLine.value,
                    singleLine = true,
                    onValueChange = { newValue -> postIntent(TagLineChanged(newValue)) },
                )
            }

            Button(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .widthIn(min = 180.dp),
                onClick = { showRegionSelector = true },
            ) {
                Text(
                    text = try {
                        Region.getById(state.selectedRegionId).title
                    } catch(exception: Exception) {
                        stringResource(id = R.string.regionSelectionPrompt)
                    },
                )
            }

            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    postIntent(
                        FindAndConfirmSummonerViewIntent(
                            gameName = state.gameName.value,
                            tagline = state.tagLine.value,
                            region = Region.getById(state.selectedRegionId)
                        )
                    )
                },
                enabled = state.gameName.isValid && state.tagLine.isValid && state.selectedRegionId != NONEXISTENT_REGION_ID
            ) {
                Text(
                    text = stringResource(id = R.string.labelFind)
                )
            }

            Spacer(modifier = Modifier.weight(0.5f))

            if(showRegionSelector) {
                ModalBottomSheet(
                    onDismissRequest = { showRegionSelector = false },
                    sheetState = regionSelectorState,
                    tonalElevation = 20.dp,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Region.entries
                            .filter { region -> region.id != state.selectedRegionId }
                            .sortedBy { region -> region.title }
                            .forEachIndexed { index, region ->
                                Text(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .clickable {
                                            scope.launch { regionSelectorState.hide() }
                                                .invokeOnCompletion {
                                                    if(!regionSelectorState.isVisible) {
                                                        showRegionSelector = false
                                                        postIntent(RegionSelected(region.id))
                                                    }
                                                }
                                        },
                                    text = region.title
                                )
                            }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
internal fun SummonerDataEntryScreenPreview() {
    ReportMidTheme {
        PreviewViewStateHolder.SummonerDataEntryScreen(
            SummonerDataEntryViewState(
                gameName = GameName("Lorem ipsum"),
                tagLine = TagLine("dolor"),
            )
        )
    }
}