package com.tsarsprocket.reportmid.tools

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun setSoftInputVisibility(context: Context, view: View, visibility: Boolean ) {
    val imm: InputMethodManager = context.getSystemService( Activity.INPUT_METHOD_SERVICE ) as InputMethodManager
    if( visibility ) {
        imm.showSoftInput( view, 0 )
    } else {
        imm.hideSoftInputFromWindow( view.windowToken, 0 )
    }
}