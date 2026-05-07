package com.lucaslpmoura.android_chat.room_screen

import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lucaslpmoura.android_chat.common.AndroidChatViewModel
import com.lucaslpmoura.kotlin_chat.client.KotlinChatClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class RoomViewModel(private val client : KotlinChatClient) : AndroidChatViewModel(client) {
    var roomName by mutableStateOf(client.currentRoomId)
    var errorLeavingRoom by mutableStateOf(true)

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
}