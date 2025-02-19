package com.tsarsprocket.reportmid.viewStateApi.viewIntent

import kotlinx.parcelize.Parcelize

/**
 * Usage options:
 * 1) Post to just quit
 * 2) Put on back stack to quit if the user navigates back
 */
@Parcelize
data object QuitViewIntent : ViewIntent
