package com.lucaslpmoura.android_chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lucaslpmoura.android_chat.dependencies.appModule
import com.lucaslpmoura.android_chat.login_screen.LoginScreen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

            LoginScreen()
        }

    }
}
