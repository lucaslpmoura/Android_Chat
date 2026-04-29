package com.lucaslpmoura.android_chat.login_screen


import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

class LoginScreenViewModel : ViewModel() {

    var isAirPlaneModeOn by mutableStateOf(false)

    @Composable
    fun LoginComposable(){
        if(!isAirPlaneModeOn){
            return Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    state = rememberTextFieldState(),
                    label = {Text("Username")}
                )
                Button (
                    onClick = {}
                ) {
                    Text("Join")
                }
            }
        } else{
            return Text("Por favor desligue o modo avião.")
        }


    }
}