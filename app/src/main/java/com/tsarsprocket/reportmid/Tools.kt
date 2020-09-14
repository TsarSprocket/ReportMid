package com.tsarsprocket.reportmid

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import kotlin.math.roundToInt

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

// UI helpers

fun setSoftInputVisibility( context: Context, view: View, visibility: Boolean ) {
    val imm: InputMethodManager = context.getSystemService( Activity.INPUT_METHOD_SERVICE ) as InputMethodManager
    if( visibility ) {
        imm.showSoftInput( view, 0 )
    } else {
        imm.hideSoftInputFromWindow( view.windowToken, 0 )
    }
}

// Text tools

fun formatLevel( level: Int ) = if( level >= 10_000_000 ) "${( level.toFloat() / 1_000_000f ).roundToInt()}M"
    else if( level >= 10_000 ) "${(level.toFloat() / 1_000f ).roundToInt()}K"
        else level.toString()

