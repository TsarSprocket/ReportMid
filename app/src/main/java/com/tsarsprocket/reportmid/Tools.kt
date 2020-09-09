package com.tsarsprocket.reportmid

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController

fun<T> Fragment.getNavigationResult( key: String = "result" ) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key) ?: throw RuntimeException( "No backstack entry found" )

fun<T> Fragment.setNavigationResult( result: T, key: String = "result") {
    val previousBackStackEntry = findNavController().previousBackStackEntry

    if( previousBackStackEntry != null ) {
        previousBackStackEntry.savedStateHandle[ key ] = result
    } else throw RuntimeException( "No previous backstack entry found" )
}