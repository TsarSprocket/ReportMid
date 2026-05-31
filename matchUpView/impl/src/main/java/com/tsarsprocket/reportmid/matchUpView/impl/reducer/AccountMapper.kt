package com.tsarsprocket.reportmid.matchUpView.impl.reducer

import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.Account
import com.tsarsprocket.reportmid.matchUpView.impl.viewState.AccountInfo
import javax.inject.Inject

internal class AccountMapper @Inject constructor() {

    fun map(from: Account): AccountInfo = AccountInfo(name = "${from.gameName}#${from.tagLine}")
}
