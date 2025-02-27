package com.tsarsprocket.reportmid.kspProcessor.util

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration

inline fun <reified AnnotationClass> Resolver.doOnEachAnnotated(noinline action: (KSClassDeclaration) -> Unit): Sequence<KSClassDeclaration> {
    return getSymbolsWithAnnotation(AnnotationClass::class.qualifiedName!!)
        .filterIsInstance<KSClassDeclaration>()
        .onEach(action)
}