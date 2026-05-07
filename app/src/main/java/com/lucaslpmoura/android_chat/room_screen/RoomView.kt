package com.lucaslpmoura.android_chat.room_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lucaslpmoura.android_chat.common.showErrorSnackbar
import org.koin.androidx.compose.koinViewModel

@Composable
fun RoomScreen(
    viewModel : RoomViewModel = koinViewModel(),
    navigateToRoomList : () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
            modifier = Modifier.padding(padding)
        ) {
            Text("Connected to room ${viewModel.roomName}")
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