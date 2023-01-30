package com.shashankbhat.splitbill.util.extension

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager


const val ACTION_BC_MANAGER =
    "com.shashankbhat.splitbill.action.ACTION_FOR_BC_MANAGER"

fun Context.regLocalBroadcastManager(
    mMessageReceiver: BroadcastReceiver
) {
    val localBroadcastManager = LocalBroadcastManager.getInstance(this)
    // Register Local Broadcast Receiver
    localBroadcastManager.registerReceiver(
        mMessageReceiver,
        IntentFilter(ACTION_BC_MANAGER)
    )
}

fun Context.unRegLocalBroadcastManager(mMessageReceiver: BroadcastReceiver) {
    val localBroadcastManager = LocalBroadcastManager.getInstance(this)
    localBroadcastManager.unregisterReceiver(mMessageReceiver)
}


fun Context.sendMessages(message: String){
    val intent = Intent(ACTION_BC_MANAGER)
    intent.putExtra("message", message)

    val localBroadcastManager = LocalBroadcastManager.getInstance(this)
    localBroadcastManager.sendBroadcast(intent)
}
