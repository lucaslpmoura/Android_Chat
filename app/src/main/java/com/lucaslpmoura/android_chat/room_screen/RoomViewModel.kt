package com.lucaslpmoura.android_chat.room_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lucaslpmoura.android_chat.common.AndroidChatViewModel
import com.lucaslpmoura.kotlin_chat.client.KotlinChatClient

class RoomViewModel(private val client : KotlinChatClient) : AndroidChatViewModel(client) {
    var roomName by mutableStateOf(client.currentRoomId)
}