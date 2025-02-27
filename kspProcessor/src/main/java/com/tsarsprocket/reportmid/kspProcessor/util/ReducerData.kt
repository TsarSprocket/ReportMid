package com.tsarsprocket.reportmid.kspProcessor.util

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile

class ReducerData(
    val declaration: KSClassDeclaration,
    val viewIntents: List<BriefProcessee>,
    val capability: String? = null,
    val viewIntentFiles: List<KSFile>,
) {
    var isAssigned = false
}
