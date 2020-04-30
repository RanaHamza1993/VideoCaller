package com.havelisolutions.videocaller.extensions

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty

fun Context.showSuccessMessage(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    try {
        Toasty.success(this, message.toString(), duration, true).show()

    } catch (e: WindowManager.BadTokenException) {
    }
}

fun Context.showInfoMessage(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    try {
        Toasty.info(this, message.toString(), duration, true).show()
    } catch (e: Exception) {
    }
}

fun Context.showErrorMessage(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    try {
        Toasty.error(this, message.toString(), duration, true).show()
    } catch (e: WindowManager.BadTokenException) {

    }
}
//View Extensions
fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.snackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }.show()
    }

}
fun TextView.clear(){
    this.text = ""
}

fun View.makeRound(){
    this.clipToOutline=true
}
//EditTextEextensions

fun EditText.getString():String{
    return this.text.toString().trim()
}
