package com.tsarsprocket.reportmid.lazy_proxy_ksp.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class LazyProxyProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        environment.logger.warn("Providing LazyProxyProcessor")
        return LazyProxyProcessor(environment.codeGenerator, environment.logger)
    }
}