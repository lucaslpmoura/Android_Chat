package com.lucaslpmoura.android_chat.dependencies

import com.lucaslpmoura.android_chat.login_screen.LoginViewModel
import com.lucaslpmoura.android_chat.room_screen.RoomViewModel
import com.lucaslpmoura.android_chat.rooms_list_screen.RoomListViewModel

import com.lucaslpmoura.kotlin_chat.client.KotlinChatClient
import org.koin.core.module.dsl.viewModel


import org.koin.dsl.module


val appModule = module {
    single { KotlinChatClient() }
    viewModel { LoginViewModel(get()) }
    viewModel { RoomListViewModel(get()) }
    viewModel { RoomViewModel(get()) }
}