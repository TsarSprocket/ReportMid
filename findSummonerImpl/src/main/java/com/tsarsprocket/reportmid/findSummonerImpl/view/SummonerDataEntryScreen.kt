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
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.lol.api.model.Region.Companion.ID_NONEXISTENT_REGION
import com.tsarsprocket.reportmid.lol.api.model.RegionInfo
import com.tsarsprocket.reportmid.lol.api.model.isGameNameValid
import com.tsarsprocket.reportmid.lol.api.model.isTagLineValid
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.PreviewViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ViewStateHolder.SummonerDataEntryScreen(
    modifier: Modifier,
    state: SummonerDataEntryViewState,
    regionInfoFactory: RegionInfo.Factory,
) {
    Scaffold(modifier = modifier) { pagePadding ->
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
                    value = state.gameName,
                    singleLine = true,
                    onValueChange = { newValue -> postIntent(GameNameChanged(newValue)) },
                )

                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = stringResource(id = R.string.addAccountImplHash),
                )

                OutlinedTextField(
                    modifier = Modifier.weight(0.3f),
                    value = state.tagLine,
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
                    text = if(Region.byId[state.selectedRegionId] != null) {
                        regionInfoFactory.get(state.selectedRegionId).title
                    } else {
                        stringResource(id = R.string.regionSelectionPrompt)
                    },
                )
            }

            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    postIntent(
                        FindAndConfirmSummonerViewIntent(
                            gameName = state.gameName,
                            tagline = state.tagLine,
                            region = Region.getById(state.selectedRegionId),
                        )
                    )
                },
                enabled = isGameNameValid(state.gameName) && isTagLineValid(state.tagLine) && state.selectedRegionId != ID_NONEXISTENT_REGION
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
                            .map { region -> regionInfoFactory.get(region.id) }
                            .sortedBy { regionInfo -> regionInfo.title }
                            .forEach { regionInfo ->
                                Text(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .clickable {
                                            scope.launch { regionSelectorState.hide() }
                                                .invokeOnCompletion {
                                                    if(!regionSelectorState.isVisible) {
                                                        showRegionSelector = false
                                                        postIntent(RegionSelected(regionInfo.regionId))
                                                    }
                                                }
                                        },
                                    text = regionInfo.title
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
            modifier = Modifier,
            state = SummonerDataEntryViewState(
                gameName = "Lorem ipsum",
                tagLine = "dolor",
            ),
            object : RegionInfo.Factory {
                override fun get(regionId: Int): RegionInfo = error("Should not be called")
            },
        )
    }
}