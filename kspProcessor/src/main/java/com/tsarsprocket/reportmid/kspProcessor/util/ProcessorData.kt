package com.tsarsprocket.reportmid.kspProcessor.util

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile

class ProcessorData(
    val declaration: KSClassDeclaration,
    val processees: List<NameInfo>,
    val capability: String? = null,
    val processeeFiles: List<KSFile>,
) {
    var isAssigned = false
}
