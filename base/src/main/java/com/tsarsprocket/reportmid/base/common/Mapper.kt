package com.tsarsprocket.reportmid.base.common

fun interface Mapper<FromType, ToType> {
    operator fun invoke(from: FromType): ToType
}