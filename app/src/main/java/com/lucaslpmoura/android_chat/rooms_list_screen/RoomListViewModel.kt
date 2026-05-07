package com.lucaslpmoura.android_chat.rooms_list_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lucaslpmoura.kotlin_chat.client.KotlinChatClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class RoomListViewModel(private val client : KotlinChatClient) : ViewModel() {

    val name = client.name

    var roomList by mutableStateOf(mapOf<String,String>())
    private val clientScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    public fun getRoomList() {
        clientScope.launch {
            println("Getting room list...")
            println("Connected with id ${client.id}")
            try{
                client.listRooms()
                delay(200.milliseconds)
                roomList = client.serverRooms
                println("Got rooms from server: ${roomList}")
            }catch (e : Exception){
                println("Failed to list rooms: ${e.message}")
            }
        }
    }

    public fun joinRoom(roomId : String){
        // TODO
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