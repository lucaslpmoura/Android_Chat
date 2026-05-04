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


class LoginScreenViewModel : ViewModel() {

    var client = KotlinChatClient()
    var serverAddress by mutableStateOf(client.serverAddress)
    var name by mutableStateOf("")

    var isSnackBarShowing by mutableStateOf(false)

    var serverDialogValue by mutableStateOf(false)
    var isAirPlaneModeOn by mutableStateOf(false)

    val serverAddressTextFieldState = TextFieldState(
        initialText = serverAddress
    )


    public fun changeServerAddress(newAddress : String){
        serverAddress = newAddress
    }

    public fun connectToServer(){
        client.serverAddress = serverAddress
        client.connect(name)
    }

}