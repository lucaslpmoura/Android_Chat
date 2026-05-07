package com.lucaslpmoura.android_chat.room_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lucaslpmoura.android_chat.common.showErrorSnackbar
import com.lucaslpmoura.android_chat.rooms_list_screen.RoomListItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun RoomScreen(
    viewModel : RoomViewModel = koinViewModel(),
    navigateToRoomList : () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    viewModel.checkForTexts()
    Scaffold(
        bottomBar = {
            NavigationBar(
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        viewModel.leaveRoom()
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Cancel,
                            contentDescription = "Sair"
                        )
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color.Red
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text("Connected to room ${viewModel.roomName}")

            RoomMessages(
                viewModel,
                modifier = Modifier.weight(1f)
            )

            TextFieldAndButton(viewModel)
        }

        when (viewModel.errorLeavingRoom) {
            true -> {
                if(viewModel.showErrorSnackbar){
                    showErrorSnackbar(snackbarHostState, scope, viewModel)
                }

            }
            false -> navigateToRoomList()
        }
    }
}

@Composable
fun RoomMessages(viewModel : RoomViewModel, modifier: Modifier){
    LazyColumn(
        modifier = modifier
    ) {
        items(viewModel.roomTexts.size) {item ->
            Text(
                "${viewModel.roomTexts[item]["origin"]}: " +
                        "${viewModel.roomTexts[item]["text"]}"
            )
        }
    }
}

@Composable
fun TextFieldAndButton(viewModel: RoomViewModel){
    Row() {
        TextField(
            state = viewModel.messageTextFieldState,
            label = { Text("Escreva aqui...") },
        )
        IconButton(
            onClick = {
                viewModel.text()
            }
        ) {
            Icon(
                Icons.Filled.Send,
                contentDescription = "Enviar"
            )
        }
    }
}