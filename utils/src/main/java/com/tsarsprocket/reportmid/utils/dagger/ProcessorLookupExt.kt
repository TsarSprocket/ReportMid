package com.tsarsprocket.reportmid.utils.dagger

import javax.inject.Provider

fun <Event : Any, Processor> Map<Class<out Event>, Provider<Processor>>.findProcessor(event: Event): Processor {
    return findProcessorOrNull(event) ?: throw ProcessorNotFoundException(event)
}

fun <Event : Any, Processor> Map<Class<out Event>, Provider<Processor>>.findProcessorOrNull(event: Event): Processor? {
    var classes = listOf<Class<*>>(event.javaClass)
    var nextLevel: MutableList<Class<*>>

    while(classes.isNotEmpty()) {
        nextLevel = mutableListOf()
        classes.onEach { clazz ->
            val provider = get(clazz)

            if(provider != null) {
                return provider.get()
            } else {
                nextLevel += listOfNotNull(clazz.superclass) + clazz.interfaces
            }
        }
        classes = nextLevel
    }

    return null
}

class ProcessorNotFoundException(val event: Any) : RuntimeException("No processor fount for ${event::class.qualifiedName}")
