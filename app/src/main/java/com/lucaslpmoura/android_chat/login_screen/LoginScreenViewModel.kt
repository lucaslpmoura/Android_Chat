package com.lucaslpmoura.android_chat.login_screen


import android.app.AlertDialog
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

import com.lucaslpmoura.kotlin_chat.client.KotlinChatClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


class LoginScreenViewModel : ViewModel() {

    var client = KotlinChatClient()
    var serverAddress by mutableStateOf(client.serverAddress)
    var name by mutableStateOf("")

    var connectingToServer by mutableStateOf(false)
    var showErrorSnackbar by mutableStateOf(false)
    var errorSnackBarText by mutableStateOf("")

    var serverDialogValue by mutableStateOf(false)
    var isAirPlaneModeOn by mutableStateOf(false)

    private val clientScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    val serverAddressTextFieldState = TextFieldState(
        initialText = serverAddress
    )


    public fun changeServerAddress(newAddress : String){
        serverAddress = newAddress
    }

    public fun connectToServer(){
        client.serverAddress = serverAddress
        connectingToServer = true
        clientScope.launch {
            try {
                delay(2.seconds)
                client.run()
            } catch (e: Exception) {
                connectingToServer = false
                errorSnackBarText = "Could not connect to server."
                showErrorSnackbar = true
            }
        }





    }

}