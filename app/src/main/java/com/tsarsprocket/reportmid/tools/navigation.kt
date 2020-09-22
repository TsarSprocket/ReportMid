package com.tsarsprocket.reportmid.tools

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController

// Navigation backstack operations

fun <T> Fragment.getNavigationResult(key: String = "result"): MutableLiveData<T> {
    val currentBackStackEntry = findNavController().currentBackStackEntry
    return currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key) ?: throw RuntimeException("No backstack entry found")
}

fun <T> Fragment.peekNavigationResult(key: String = "result"): T? {
    val currentBackStackEntry = findNavController().currentBackStackEntry
    return currentBackStackEntry?.savedStateHandle?.get<T>(key)
}

fun <T> Fragment.setNavigationResult( result: T, key: String = "result" ) {
    val previousBackStackEntry = findNavController().previousBackStackEntry

    if( previousBackStackEntry != null ) {
        previousBackStackEntry.savedStateHandle.set(key,result)
    } else throw RuntimeException("No previous backstack entry found")
}

fun <T> Fragment.removeNavigationResult( key: String = "result" ): T? {
    val currentBackStackEntry = findNavController().currentBackStackEntry
    return currentBackStackEntry?.savedStateHandle?.remove<T>(key)
}

fun <T> Fragment.getPermVar( varName: String ): T? {
    val currentBackStackEntry = findNavController().currentBackStackEntry
    return currentBackStackEntry?.savedStateHandle?.get<T>( varName )
}

fun <T> Fragment.setPermVar( varName: String, value: T ) {
    val currentBackStackEntry = findNavController().currentBackStackEntry
    currentBackStackEntry?.savedStateHandle?.set( varName, value )
}

fun <T> Fragment.removePermVar( varName: String ): T? {
    val currentBackStackEntry = findNavController().currentBackStackEntry
    return currentBackStackEntry?.savedStateHandle?.remove<T>( varName )
}

// Text tools

