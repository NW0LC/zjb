package com.exz.zjb.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.exz.zjb.module.MainActivity


class NotificationClickReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val intents = Intent(context, MainActivity::class.java)
        intents.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        context.startActivity(intents)

    }

}
