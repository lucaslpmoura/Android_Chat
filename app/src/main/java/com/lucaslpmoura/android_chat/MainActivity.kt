package com.lucaslpmoura.android_chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.lucaslpmoura.android_chat.dependencies.appModule
import com.lucaslpmoura.android_chat.login_screen.LoginScreen
import kotlinx.serialization.Serializable
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {

    @Serializable
    object Login



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = Login) {
                composable<Login> {LoginScreen()}
            }
        }

    }
}
