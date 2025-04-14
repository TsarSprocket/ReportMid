package com.tsarsprocket.reportmid.profileOverviewImpl.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.utils.compose.SkeletonRectangle

@Composable
internal fun ProfileOverviewLoading(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 36.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        SkeletonRectangle(
            modifier = Modifier.size(128.dp),
        )

        SkeletonRectangle(
            modifier = Modifier
                .padding(top = 52.dp)
                .size(width = 120.dp, height = 16.dp),
        )

        SkeletonRectangle(
            modifier = Modifier
                .padding(top = 24.dp)
                .size(width = 64.dp, height = 16.dp),
        )

        SkeletonMasteries(
            modifier = Modifier.padding(top = 50.dp),
        )

        Spacer(modifier = Modifier.weight(1.2f))
    }
}

@Composable
private fun SkeletonMasteries(modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        repeat(5) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally,
            ) {
                SkeletonRectangle(
                    modifier = Modifier.size(48.dp),
                )

                SkeletonRectangle(
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .size(width = 14.dp, height = 12.dp),
                )

                SkeletonRectangle(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(width = 40.dp, height = 10.dp),
                )
            }
        }
    }
}


@Composable
@Preview
private fun ProfileOverviewLoadingPreview() {
    ReportMidTheme {
        ProfileOverviewLoading(Modifier.fillMaxSize())
    }
}