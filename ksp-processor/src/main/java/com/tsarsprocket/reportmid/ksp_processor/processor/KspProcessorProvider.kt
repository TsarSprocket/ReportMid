package com.tsarsprocket.reportmid.ksp_processor.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class KspProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        environment.logger.warn("Providing LazyProxyProcessor")
        return KspProcessor(environment.codeGenerator, environment.logger)
    }
}