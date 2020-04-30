package com.havelisolutions.videocaller.generics

import kotlinx.coroutines.*

object Coroutines {
    fun main(work: suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            work()
        }


    fun <T : Any?> ioThenMain(work: suspend () -> T, callback: (T?) -> Unit) =
        CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async rt@{
                return@rt work()
            }.await()
            callback(data)
        }
    fun <T : Any?> defaultThenMain(work: suspend () -> T, callback: (T?) -> Unit) =
        CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.Default).async rt@{
                return@rt work()
            }.await()
            callback(data)
        }

}