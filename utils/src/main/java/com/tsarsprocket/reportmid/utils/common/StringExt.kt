package com.tsarsprocket.reportmid.utils.common

val String.noWhitespaces
    get() = this.filterNot { it.isWhitespace() }