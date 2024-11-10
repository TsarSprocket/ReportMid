package com.tsarsprocket.reportmid.findSummonerImpl.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import com.tsarsprocket.reportmid.lol.model.GameName
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.lol.model.TagLine
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.utils.common.EMPTY_STRING
import kotlinx.coroutines.launch

const val NONEXISTENT_REGION_ID = -1L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SummonerDataEntryScreen(onFindSummonerClick: (GameName, TagLine, Region) -> Unit) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var gameName by remember { mutableStateOf(GameName(EMPTY_STRING)) }
            var tagline by remember { mutableStateOf(TagLine(EMPTY_STRING)) }
            var selectedRegionId by remember { mutableLongStateOf(NONEXISTENT_REGION_ID) }
            var showRegionSelector by remember { mutableStateOf(false) }
            val regionSelectorState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()

            Spacer(modifier = Modifier.weight(0.3f))

            Row(
                modifier = Modifier.fillMaxWidth(0.7f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(0.7f),
                    value = gameName.value,
                    onValueChange = { gameName = GameName(it) },
                )

                Text(
                    text = stringResource(id = R.string.addAccountImplHash),
                )

                OutlinedTextField(
                    modifier = Modifier.weight(0.3f),
                    value = tagline.value,
                    onValueChange = { tagline = TagLine(it) },
                )
            }

            Button(
                onClick = { showRegionSelector = true }
            ) {
                Text(
                    text = try {
                        Region.getById(selectedRegionId).title
                    } catch(exception: Exception) {
                        stringResource(id = R.string.regionSelectionPrompt)
                    }
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            Button(
                onClick = {
                    onFindSummonerClick(gameName, tagline, Region.getById(selectedRegionId))
                },
                enabled = gameName.isValid && tagline.isValid && selectedRegionId != NONEXISTENT_REGION_ID
            ) {
                Text(
                    text = stringResource(id = R.string.labelFind)
                )
            }

            Spacer(modifier = Modifier.weight(0.6f))

            if(showRegionSelector) {
                ModalBottomSheet(
                    onDismissRequest = { showRegionSelector = false },
                    sheetState = regionSelectorState
                ) {
                    Column {
                        Region.entries
                            .filter { region -> region.id != selectedRegionId }
                            .sortedBy { region -> region.title }
                            .forEach { region ->
                                Text(
                                    modifier = Modifier.clickable {
                                        scope.launch { regionSelectorState.hide() }
                                            .invokeOnCompletion {
                                                if(!regionSelectorState.isVisible) {
                                                    showRegionSelector = false
                                                    selectedRegionId = region.id
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
        SummonerDataEntryScreen { _, _, _ -> }
    }
}