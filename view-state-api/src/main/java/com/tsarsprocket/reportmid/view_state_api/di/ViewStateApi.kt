package com.tsarsprocket.reportmid.view_state_api.di

import com.tsarsprocket.reportmid.base.di.Api
import com.tsarsprocket.reportmid.base.di.FragmentsCreator

interface ViewStateApi : Api, FragmentsCreator, StateVisualizersProvider, EffectHandlersProvider, StateReducersProvider
