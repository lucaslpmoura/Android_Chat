package com.lucaslpmoura.android_chat.rooms_list_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.ActivityNavigator
import androidx.navigation.Navigation
import com.lucaslpmoura.android_chat.common.showErrorSnackbar
import com.lucaslpmoura.android_chat.login_screen.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.milliseconds

@Composable
public fun RoomListScreen(
    viewModel: RoomListViewModel = koinViewModel(),
    navigateToLogin : () -> Unit,
    navigateToRoom : () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
            Spacer(Modifier.height(10.dp))
            RoomList(viewModel, navigateToRoom)
            Spacer(Modifier.weight(1f))
            Text(
                "Conectado como ${viewModel.name}",
                Modifier
                    .navigationBarsPadding()
                    .padding(bottom = 10.dp)
            )
        }


        when(viewModel.errorJoiningRoom){
            true -> {
                if(viewModel.showErrorSnackbar)
                    showErrorSnackbar(snackbarHostState, scope, viewModel)
            }
            false -> navigateToRoom()
        }

    }
}

@Composable
public fun RoomList(
    viewModel: RoomListViewModel,
    navigateToRoom: () -> Unit
){
    LazyColumn() {
        items(viewModel.roomList.size) { item ->
            val room = mapOf<String,String>(
                "id" to viewModel.roomList.keys.elementAt(item),
                "name" to  viewModel.roomList.values.elementAt(item)
            )
            RoomListItem(viewModel, room)
            Spacer(Modifier.height(30.dp))


        }
    }
}

@Composable
public fun RoomListItem(
    viewModel : RoomListViewModel,
    room : Map<String,String>,
){
    val roomId : String = room["id"]!!
    val roomName : String = room["name"]!!
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ){
        Text(roomName)
        Spacer(Modifier.size(width = 100.dp, height = 1.dp))
        Button(
            onClick = {
                viewModel.joinRoom(roomId)
            }
        ) {
            Text("Entrar...")
        }


    }


}

