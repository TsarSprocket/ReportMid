package com.tsarsprocket.reportmid.landingImpl.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tsarsprocket.reportmid.landingImpl.R
import com.tsarsprocket.reportmid.theme.PreviewTheme
import com.tsarsprocket.reportmid.theme.reportMidFontFamily


@Composable
fun Landing() {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.width(300.dp),
                painter = painterResource(id = R.drawable.logo),
                contentScale = ContentScale.FillWidth,
                contentDescription = stringResource(id = R.string.landing_logo_description),
            )

            Spacer(
                modifier = Modifier.requiredHeight(40.dp),
            )

            Text(
                text = stringResource(id = R.string.landing_message),
                fontFamily = reportMidFontFamily,
                fontSize = 24.sp,
                color = Color(0xFFE6EE9C),
            )

            Spacer(
                modifier = Modifier.requiredHeight(70.dp),
            )

            CircularProgressIndicator(
                modifier = Modifier.size(60.dp),
                color = Color(0xFFE6EE9C),
                progress = Float.MAX_VALUE,
            )
        }
    }
}

@Preview()
@Composable
fun LandingPreview() {
    PreviewTheme {
        Landing()
    }
}