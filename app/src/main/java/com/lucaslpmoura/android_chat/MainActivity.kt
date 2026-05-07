package com.lucaslpmoura.android_chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.lucaslpmoura.android_chat.login_screen.LoginScreen
import com.lucaslpmoura.android_chat.rooms_list_screen.RoomListScreen
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    @Serializable
    object Login
    @Serializable
    object RoomList


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = Login) {
                composable<Login> { LoginScreen(
                    navigateToRoomsList = { navController.navigate(route = RoomList)}
                )}
                composable<RoomList> { RoomListScreen(
                    navigateToLogin = {navController.navigate(route = Login)}
                )}
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }
}
