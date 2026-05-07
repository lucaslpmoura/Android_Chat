package com.lucaslpmoura.android_chat.rooms_list_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lucaslpmoura.android_chat.common.AndroidChatViewModel
import com.lucaslpmoura.kotlin_chat.client.KotlinChatClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class RoomListViewModel(private val client : KotlinChatClient) : AndroidChatViewModel(client) {

    var roomList by mutableStateOf(mapOf<String,String>())
    var errorJoiningRoom by mutableStateOf(true)

    public fun getRoomList() {
        clientScope.launch {
            println("Getting room list...")
            println("Connected with id ${client.id}")
            try{
                client.listRooms()
                delay(200.milliseconds)
                roomList = client.serverRooms
                println("Got rooms from server: $roomList")
            }catch (e : Exception){
                println("Failed to list rooms: ${e.message}")
            }
        }
    }

    public fun joinRoom(roomId : String){
        clientScope.launch {
            try{
                client.joinRoom(roomId)


                var i = 0
                while(client.currentRoomId == null && i < 10){
                    delay(100.milliseconds)
                    i++
                }
                println(client.currentRoomId)
                if(client.lastError != null){
                    throw Exception("Server returned error.")
                }

                if(client.currentRoomId != roomId){
                    throw Exception("Server did not let you join room $roomId.")
                }

                errorJoiningRoom = false

            }catch (e : Exception){
                errorJoiningRoom = true
                errorSnackBarText = e.message!!
                showErrorSnackbar = true
            }
        }

    }

    public fun disconnect(){
        clientScope.launch {
            try{
                println("Sending DISCONNECT to server....")
                client.disconnect()
            } catch (e : Exception){
                println(e.message)
            }
        }
    }
}