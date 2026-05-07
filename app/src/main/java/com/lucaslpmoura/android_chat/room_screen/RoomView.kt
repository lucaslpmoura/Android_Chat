package com.lucaslpmoura.android_chat.room_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun RoomScreen(viewModel : RoomViewModel = koinViewModel()) {
    Scaffold(

    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Text("Connected to room ${viewModel.roomName}")
        }
    }
}