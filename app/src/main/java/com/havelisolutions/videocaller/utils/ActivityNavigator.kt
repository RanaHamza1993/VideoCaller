package com.havelisolutions.videocaller.utils

import android.content.Context
import android.content.Intent

class ActivityNavigator<T>{
    constructor(from: Context, to:Class<T>){
        val intent= Intent(from,to)
        from.startActivity(intent)

    }
    constructor(from: Context, to: Class<T>, extra: Int) {
        val intent = Intent(from, to)
        intent.putExtra("extra", extra)
        from.startActivity(intent)
    }
    constructor(from: Context, to:Class<T>,hasFlag:Boolean){
        val intent= Intent(from,to)
        // set the new task and clear flags
        if(hasFlag)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        from.startActivity(intent)

    }
}