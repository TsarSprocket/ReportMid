package com.tsarsprocket.reportmid.base_api.common

fun interface Mapper<FromType, ToType> {
    operator fun invoke(from: FromType): ToType
}