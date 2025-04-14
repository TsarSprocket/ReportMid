package com.tsarsprocket.reportmid.landingImpl.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tsarsprocket.reportmid.landingImpl.R
import com.tsarsprocket.reportmid.theme.ReportMidTheme
import com.tsarsprocket.reportmid.theme.reportMidColorScheme
import com.tsarsprocket.reportmid.theme.reportMidFontFamily


@Composable
fun LandingScreen(modifier: Modifier) {
    Scaffold(modifier = modifier) { pagePadding ->
        Column(
            modifier = Modifier
                .padding(pagePadding)
                .fillMaxSize()
                .background(color = reportMidColorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.width(300.dp),
                painter = painterResource(id = R.drawable.logo),
                contentScale = ContentScale.FillWidth,
                contentDescription = stringResource(id = R.string.landing_logo_description),
            )

            Text(
                modifier = Modifier.padding(top = 40.dp),
                text = stringResource(id = R.string.landing_message),
                fontFamily = reportMidFontFamily,
                fontSize = 24.sp,
                color = reportMidColorScheme.onBackground,
            )

            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 70.dp)
                    .size(60.dp),
                color = reportMidColorScheme.onBackground,
                progress = { Float.MAX_VALUE },
            )
        }
    }
}

@Preview()
@Composable
fun LandingPreview() {
    ReportMidTheme {
        LandingScreen(Modifier)
    }
}