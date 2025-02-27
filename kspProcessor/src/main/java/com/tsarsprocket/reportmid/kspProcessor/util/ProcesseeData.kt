package com.tsarsprocket.reportmid.kspProcessor.util

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile

class ProcesseeData(
    val declaration: KSClassDeclaration,
    val fqProcessorName: String?,
    val file: KSFile,
) {
    var isAssigned = false
}