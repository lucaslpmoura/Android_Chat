package com.lucaslpmoura.android_chat.dependencies

import com.lucaslpmoura.android_chat.login_screen.LoginScreenViewModel

import com.lucaslpmoura.kotlin_chat.client.KotlinChatClient
import org.koin.core.module.dsl.viewModel


import org.koin.dsl.module


val appModule = module {
    single { KotlinChatClient() }
    viewModel { LoginScreenViewModel(get()) }
}