package com.lucaslpmoura.android_chat.room_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.clearText
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucaslpmoura.android_chat.common.showErrorSnackbar
import com.lucaslpmoura.android_chat.rooms_list_screen.RoomListItem
import com.lucaslpmoura.android_chat.ui.theme.Purple40
import com.lucaslpmoura.android_chat.ui.theme.PurpleGrey40
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Box(
                modifier = Modifier
                    .height(75.dp)
                    .fillMaxWidth()
                    .background(Purple40)
                    .statusBarsPadding(),
                contentAlignment = Alignment.Center

            ){
                Text(
                    "${viewModel.roomName}",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(
                        bottom = 10.dp
                    )
                )
            }

            RoomMessages(
                viewModel,
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = padding.calculateStartPadding(LayoutDirection.Ltr),
                        end = padding.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = padding.calculateBottomPadding()
                    )
            )

            TextFieldAndButton(viewModel, padding)
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
            .padding(vertical = 5.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start

    ) {
        items(viewModel.roomTexts.size) {item ->
            TextMessage(
                viewModel,
                viewModel.roomTexts[item]
            )
        }
    }
}

@Composable
fun TextMessage(
    viewModel: RoomViewModel,
    text : Map<String,String>,
){
    val backcolor = Color.LightGray
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .padding(5.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
                .background(backcolor),
            contentAlignment = Alignment.Center
        ) {
            Text(text["origin"]!!)
        }
        Box(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight()
                .background(Color.DarkGray)
        ){}
        Box(
            modifier = Modifier
                .weight(9f)
                .fillMaxHeight()
                .background(backcolor),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text["text"]!!,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

@Composable
fun TextFieldAndButton(viewModel: RoomViewModel, padding : PaddingValues){
    Row(
        Modifier.fillMaxWidth()
                .padding(bottom = padding.calculateBottomPadding())
    ) {
        TextField(
            state = viewModel.messageTextFieldState,
            label = { Text("Escreva aqui...") },
            modifier = Modifier.weight(5f)
        )
        IconButton(
            onClick = {
                viewModel.text()
                viewModel.messageTextFieldState.clearText()
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Filled.Send,
                contentDescription = "Enviar"
            )
        }
    }
}