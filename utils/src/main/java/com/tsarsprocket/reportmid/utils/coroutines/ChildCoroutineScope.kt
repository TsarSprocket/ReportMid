package com.tsarsprocket.reportmid.utils.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob


fun ChildCoroutineScope(parent: CoroutineScope) = CoroutineScope(parent.coroutineContext + Job(parent.coroutineContext[Job]))

fun SupervisorChildCoroutineScope(parent: CoroutineScope) = CoroutineScope(parent.coroutineContext + SupervisorJob(parent.coroutineContext[Job]))
