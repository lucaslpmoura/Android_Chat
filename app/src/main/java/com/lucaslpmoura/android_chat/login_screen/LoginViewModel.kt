package com.lucaslpmoura.android_chat.login_screen

import android.content.ActivityNotFoundException
import com.lucaslpmoura.kotlin_chat.client.KotlinChatClient

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lucaslpmoura.android_chat.common.AndroidChatViewModel


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


class LoginViewModel(private val client : KotlinChatClient) : AndroidChatViewModel(client) {
    var name by mutableStateOf("")
    var serverAddress by mutableStateOf("10.0.2.2")
    var connectionState by mutableStateOf(ConnectionState.NOT_CONNECTED)

    var serverDialogValue by mutableStateOf(false)
    var isAirPlaneModeOn by mutableStateOf(false)

    val userNameTextState = TextFieldState()
    val serverAddressTextFieldState = TextFieldState(
        initialText = serverAddress
    )


    public fun changeServerAddress(newAddress : String){
        serverAddress = newAddress
    }

    public fun connectToServer(){
        name = userNameTextState.text.toString()

        client.serverAddress = serverAddress
        connectionState = ConnectionState.CONNECTING

        clientScope.launch {
            try {
                if(name.isEmpty()){
                    throw Exception("User name cannot be empty.")
                }

                delay(2.seconds)
                client.run()
                client.connect(name)
                delay(200.milliseconds)


                if(client.lastError != null){
                    throw Exception("Server returned error.")
                }
                if(client.name != name){
                    throw Exception("Server rejected connection.")
                }


                connectionState = ConnectionState.CONNECTED


            } catch (e: Exception) {
                connectionState = ConnectionState.NOT_CONNECTED
                errorSnackBarText = e.message!!
                showErrorSnackbar = true
            }
        }
    }


    public fun updateUIOnAirPlaneModeChange(context : Context) {
        this.isAirPlaneModeOn = checkAirPlaneMode(context)
    }

    fun checkAirPlaneMode(context: Context?): Boolean {
        return Settings.Global.getInt(
            context?.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON
        ) != 0
    }

    public fun shareOnWhatsapp(context: Context) {
        Intent(Intent.ACTION_SEND).also {
            it.setType("text/plain")
            it.setPackage("com.whatsapp")
            it.putExtra(Intent.EXTRA_TEXT, "Venha usar o android chat!")
            try {
                context.startActivity(Intent.createChooser(it, "Compratilhar com:"))
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }

        }
    }

    public fun sendBugFixEmail(context: Context) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Only email apps handle this.
            putExtra(Intent.EXTRA_EMAIL, arrayOf("lucas.moura@setis.com.br"))
            putExtra(Intent.EXTRA_SUBJECT, "[ANDROID_CHAT][BUG]")
            putExtra(Intent.EXTRA_TEXT, "Descreva seu bug aqui.")
        }
        try {
            context.startActivity(Intent.createChooser(intent, "Enviar relatório de bug"))
        } catch (e: ActivityNotFoundException) {
            println("Could not resolve the activity.")
            e.printStackTrace()
        }
    }


    enum class ConnectionState {
        NOT_CONNECTED, CONNECTING, CONNECTED
    }

}

