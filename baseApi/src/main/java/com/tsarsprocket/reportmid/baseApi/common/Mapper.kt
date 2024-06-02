package com.tsarsprocket.reportmid.baseApi.common

fun interface Mapper<FromType, ToType> {
    operator fun invoke(from: FromType): ToType
}