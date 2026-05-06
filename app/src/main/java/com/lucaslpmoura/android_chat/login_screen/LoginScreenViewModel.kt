package com.lucaslpmoura.android_chat.login_screen


import android.app.AlertDialog
import android.content.Context
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.lucaslpmoura.android_chat.common.getChatClient

import com.lucaslpmoura.kotlin_chat.client.KotlinChatClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


class LoginScreenViewModel(private val client : KotlinChatClient) : ViewModel() {

    var serverAddress by mutableStateOf(client.serverAddress)
    var name by mutableStateOf("")

    var connectionState by mutableStateOf(ConnectionState.NOT_CONNECTED)
    var showErrorSnackbar by mutableStateOf(false)
    var errorSnackBarText by mutableStateOf("")

    var serverDialogValue by mutableStateOf(false)
    var isAirPlaneModeOn by mutableStateOf(false)

    private val clientScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    val userNameTextState = TextFieldState(

    )
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

}

enum class ConnectionState {
    NOT_CONNECTED, CONNECTING, CONNECTED
}