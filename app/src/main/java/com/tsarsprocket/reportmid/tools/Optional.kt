package com.tsarsprocket.reportmid.tools

sealed class Optional<T> {

    abstract val value: T

    abstract val hasValue: Boolean

    fun<R> ifHasValue( lambda: ((T)->R) ) = lambda.invoke( value )

    data class OptionalValue<T>( override val value: T ) : Optional<T>() {
        override val hasValue = true
    }

    class OptionalEmpty<T> : Optional<T>() {
        override val value: T
            get() = throw RuntimeException( "Empty optional does not contain value" )

        override val hasValue = false
    }

    companion object {
        fun<T> empty() = OptionalEmpty<T>()
        fun<T> T.optional() = OptionalValue( this )
    }
}