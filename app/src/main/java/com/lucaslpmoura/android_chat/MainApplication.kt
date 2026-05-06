package com.lucaslpmoura.android_chat

import android.app.Application
import androidx.compose.runtime.currentRecomposeScope
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.lucaslpmoura.android_chat.dependencies.appModule
import com.lucaslpmoura.kotlin_chat.client.KotlinChatClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application(), KoinComponent, DefaultLifecycleObserver{

    lateinit var chatClient: KotlinChatClient
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super<Application>.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }

        chatClient = getKoin().get()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)


        applicationScope.launch {
            try{
                println("Sending DISCONNECT to server....")
                chatClient.disconnect()
            } catch (e : Exception){
                println(e.message)
            }
        }

    }
}