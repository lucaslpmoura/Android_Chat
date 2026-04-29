package com.lucaslpmoura.android_chat.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings

class AirPlaneModeReceiver(val listener: AirPlaneModeListener) : BroadcastReceiver() {

    var isTurnedOn : Boolean = false
        private set

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED){
             isTurnedOn  = Settings.Global.getInt(
                context?.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON
            ) !=0
            println("Is airplane mode enabled? $isTurnedOn")
            listener.updateUIOnAirPlaneModeChange(isTurnedOn)
        }
    }
}