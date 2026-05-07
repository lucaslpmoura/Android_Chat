package com.lucaslpmoura.android_chat.rooms_list_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon


import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.ActivityNavigator
import androidx.navigation.Navigation
import org.koin.androidx.compose.koinViewModel

@Composable
public fun RoomListScreen(
    viewModel: RoomListViewModel = koinViewModel(),
    navigateToLogin : () -> Unit
) {
    viewModel.getRoomList()

    Scaffold(
        bottomBar = {
            NavigationBar(
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        viewModel.disconnect()
                        navigateToLogin()
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Cancel,
                            contentDescription = "Sair"
                        )
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Text("Connected as ${viewModel.name}")
            RoomList(viewModel)
        }

    }
}

@Composable
public fun RoomList(viewModel: RoomListViewModel){
    LazyColumn() {
        items(viewModel.roomList.size) { item ->
            val room = mapOf<String,String>(
                "id" to viewModel.roomList.keys.elementAt(item),
                "name" to  viewModel.roomList.values.elementAt(item)
            )
            RoomListItem(viewModel, room)


        }
    }
}

@Composable
public fun RoomListItem(viewModel : RoomListViewModel, room : Map<String,String>){
    val roomId : String = room["id"]!!
    val roomName : String = room["name"]!!
    Row(
    ){
        Text(roomName)
        Button(
            onClick = {viewModel.joinRoom(roomId)}
        ) {
            Text("Entrar...")
        }
    }
}