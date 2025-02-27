package com.tsarsprocket.reportmid.kspProcessor.util

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile

class ViewIntentData(
    val declaration: KSClassDeclaration,
    val fqReducerName: String?,
    val file: KSFile,
) {
    var isAssigned = false
}