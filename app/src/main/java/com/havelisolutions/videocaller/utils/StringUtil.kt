package com.havelisolutions.videocaller.utils

import android.text.Html
import android.text.Spanned
import android.text.SpannedString
import androidx.core.text.HtmlCompat

object StringUtil {
    fun getQuantityString(quantity: Int): String? {
        return "Qty: $quantity"
    }


    fun convertIntToString(value: Int?): String? {
        if(value==null)
            return "0"
        else
        return value.toString()
    }
    fun convertDoubleToString(value: Double?): String? {
        if(value==null)
            return "0"
        else
            return value.toString()
    }
    fun convertStringToHtml(value: String): Spanned {

        return HtmlCompat.fromHtml(value, HtmlCompat.FROM_HTML_MODE_LEGACY);
    }


}