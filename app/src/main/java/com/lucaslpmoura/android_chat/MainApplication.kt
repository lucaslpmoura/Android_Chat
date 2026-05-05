package com.lucaslpmoura.android_chat

import android.app.Application
import com.lucaslpmoura.android_chat.dependencies.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}