package com.tsarsprocket.reportmid.summonerViewImpl.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tsarsprocket.reportmid.theme.reportMidColorScheme
import com.tsarsprocket.reportmid.theme.reportMidTypography

@Composable
internal fun SummonerView(modifier: Modifier, content: @Composable (Modifier) -> Unit) {
    Column(modifier = modifier) {
        content(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
        ) {
            NavigationBarItem(
                selected = true,
                onClick = { /*TODO*/ },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        text = "Profile",
                        color = reportMidColorScheme.primary,
                        style = reportMidTypography.bodyMedium,
                    )
                }
            )
        }
    }
}