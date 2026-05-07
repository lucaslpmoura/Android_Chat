package com.lucaslpmoura.android_chat.room_screen

import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lucaslpmoura.android_chat.common.AndroidChatViewModel
import com.lucaslpmoura.kotlin_chat.client.KotlinChatClient
import com.lucaslpmoura.kotlin_chat.common.KotlinChatMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class RoomViewModel(private val client : KotlinChatClient) : AndroidChatViewModel(client) {
    var roomName by mutableStateOf(client.currentRoomName)
    var roomId by mutableStateOf(client.currentRoomId)
    var errorLeavingRoom by mutableStateOf(true)

    var messageTextFieldState = TextFieldState()

    var lastText by mutableStateOf(client.lastText)
    var roomTexts = mutableStateListOf<Map<String, String>>()

    public fun leaveRoom(){
        clientScope.launch {
            try{
                client.leaveRoom()

                var i = 0
                while(client.currentRoomId != null && i < 10){
                    delay(100.milliseconds)
                    i++
                }

                if(client.lastError != null){
                    throw Exception("Server returned error: ${client.lastError!!.data}")
                }
                if(client.currentRoomId != null){
                    throw Exception("Failed to leave room")
                }

                errorLeavingRoom = false

            }catch (e : Exception){
                println("Error leaving room: ${e.message}")
                errorSnackBarText = e.message!!
                showErrorSnackbar = true
                errorLeavingRoom = true
            }
        }
    }

    public fun checkForTexts(){
        clientScope.launch {
            while(client.currentRoomId == roomId){
                if(client.lastText != null && client.lastText != lastText){
                    lastText = client.lastText
                    roomTexts.add(hashMapOf(
                        "origin" to lastText!!.origin,
                        "text" to lastText!!.data

                    ))
                    println(roomTexts)
                }
                delay(200.milliseconds)
            }
        }
    }

    public fun text(){
        clientScope.launch {
            try{
                client.text(messageTextFieldState.text.toString())

                var i = 0
                while(client.currentRoomId != null && i < 10){
                    delay(100.milliseconds)
                    i++
                }

                if(client.lastError != null){
                    throw Exception("Server returned error: ${client.lastError!!.data}")
                }
                if(client.currentRoomId == null){
                    throw Exception("Failed to send message to room")
                }



            }catch (e : Exception){
                println("Error send message: ${e.message}")

            }
        }
    }
}