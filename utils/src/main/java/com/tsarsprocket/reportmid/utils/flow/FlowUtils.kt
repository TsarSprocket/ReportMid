package com.tsarsprocket.reportmid.utils.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

fun <T> Flow<T>.hide(): Flow<T> = object : Flow<T> by this {}

fun <T> SharedFlow<T>.hide(): SharedFlow<T> = object : SharedFlow<T> by this {}

fun <T> StateFlow<T>.hide(): StateFlow<T> = object : StateFlow<T> by this {}