package com.tsarsprocket.reportmid.findSummonerImpl.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.tsarsprocket.reportmid.findSummonerImpl.R
import com.tsarsprocket.reportmid.findSummonerImpl.domain.SummonerData
import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.theme.reportMidColorScheme
import com.tsarsprocket.reportmid.utils.common.EMPTY_STRING

@Composable
internal fun ConfirmSummonerScreen(
    summonerData: SummonerData,
    confirmAction: () -> Unit,
    rejectAction: () -> Unit,
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(2f))

            Image(
                modifier = Modifier.requiredSize(size = 128.dp),
                painter = rememberAsyncImagePainter(model = summonerData.iconUrl),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = summonerData.riotId,
            )

            Spacer(modifier = Modifier.weight(3f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    text = stringResource(id = R.string.confirmationPrompt),
                )

                Row(
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 32.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        modifier = Modifier.width(width = 76.dp),
                        onClick = rejectAction,
                        colors = ButtonDefaults.buttonColors(contentColor = reportMidColorScheme.onPrimary)
                    ) {
                        Text(
                            text = stringResource(id = R.string.buttonLabelReject),
                            color = LocalContentColor.current,
                        )
                    }

                    Button(
                        modifier = Modifier
                            .padding(start = 64.dp)
                            .width(width = 76.dp),
                        onClick = confirmAction,
                    ) {
                        Text(
                            text = stringResource(id = R.string.buttonLabelConfirm),
                            color = LocalContentColor.current,
                        )
                    }
                }
            }
        }
    }
}


@Composable
@Preview
private fun ConfirmSummonerScreenPreview() {
    ReportMidTheme {
        ConfirmSummonerScreen(
            summonerData = SummonerData(
                puuid = Puuid(EMPTY_STRING),
                region = Region.RUSSIA,
                riotId = "Lorem#ipsum",
                iconUrl = "https://static.wikia.nocookie.net/lolesports_gamepedia_en/images/a/a7/MalzaharSquare.png/revision/latest?cb=20170802052150",
                level = 0,
            ),
            confirmAction = {},
            rejectAction = {},
        )
    }
}