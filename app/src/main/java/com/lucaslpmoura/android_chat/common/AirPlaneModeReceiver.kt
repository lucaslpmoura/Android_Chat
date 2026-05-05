package com.lucaslpmoura.android_chat.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings

class AirPlaneModeReceiver(val listener: AirPlaneModeListener) : BroadcastReceiver() {

    var isTurnedOn : Boolean = false
        private set

    override fun onReceive(context: Context?, intent: Intent?) {

    }


}