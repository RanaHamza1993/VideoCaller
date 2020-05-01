package com.havelisolutions.videocaller.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

object Constants {
    object keys{
        val key="key"
        val cases="cases"
        val deaths="deaths"
        val recovered="recovered"
        val COUNTRY_NAME="countryname"

    }
    object activityKeys{
        val STARTED_FROM="startedfrom"
        val SPLASH="splash"
        val NOTIFICATION="notification"
        val TITLE="title"
        val MESSAGE="message"
        val DATA="data"
    }
    fun loadImage(view: ImageView, url:String?){
        Glide.with(view)
            .setDefaultRequestOptions(provideRequestOptions())
            .load(url)
            .into(view)
    }
}