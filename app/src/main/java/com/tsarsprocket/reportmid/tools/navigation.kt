package com.tsarsprocket.reportmid.tools

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController

const val DEFAULT_NAVIGATION_RESULT_KEY = "result"

fun <T> Fragment.getNavigationReturnedValue(key: String = DEFAULT_NAVIGATION_RESULT_KEY): MutableLiveData<T> {
    val currentBackStackEntry = findNavController().currentBackStackEntry
    return currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key) ?: throw NoBackstackEntryException()
}

fun <T> Fragment.peekNavigationReturnedValue(key: String = DEFAULT_NAVIGATION_RESULT_KEY): T? {
    val currentBackStackEntry = findNavController().currentBackStackEntry
    return currentBackStackEntry?.savedStateHandle?.get<T>(key)
}

fun <T> Fragment.removeNavigationReturnedValue(key: String = DEFAULT_NAVIGATION_RESULT_KEY): T? {
    val currentBackStackEntry = findNavController().currentBackStackEntry
    return currentBackStackEntry?.savedStateHandle?.remove<T>(key)
}

fun <T> Fragment.prepareNavigationReturnedValue(result: T?, key: String = DEFAULT_NAVIGATION_RESULT_KEY) {
    val currentBackStackEntry = findNavController().currentBackStackEntry
    if (currentBackStackEntry != null) {
        currentBackStackEntry.savedStateHandle.set(key, result)
    } else throw NoBackstackEntryException()
}

fun Fragment.doesReturnedValueExist(varName: String): Boolean =
    findNavController().currentBackStackEntry?.savedStateHandle?.contains(varName) ?: false

fun <T> Fragment.setNavigationResult(result: T?, key: String = DEFAULT_NAVIGATION_RESULT_KEY) {
    val previousBackStackEntry = findNavController().previousBackStackEntry
    if (previousBackStackEntry != null) {
        previousBackStackEntry.savedStateHandle.set(key, result)
    } else throw NoBackstackEntryException()
}

fun <T> Fragment.getPermVar(varName: String): T? {
    val currentBackStackEntry = findNavController().currentBackStackEntry
    return currentBackStackEntry?.savedStateHandle?.get<T>(varName)
}

fun <T> Fragment.setPermVar(varName: String, value: T) {
    val currentBackStackEntry = findNavController().currentBackStackEntry
    currentBackStackEntry?.savedStateHandle?.set(varName, value)
}

fun <T> Fragment.removePermVar(varName: String): T? {
    val currentBackStackEntry = findNavController().currentBackStackEntry
    return currentBackStackEntry?.savedStateHandle?.remove<T>(varName)
}
