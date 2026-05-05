package com.lucaslpmoura.android_chat.common

import com.lucaslpmoura.kotlin_chat.client.KotlinChatClient

private var client : KotlinChatClient? = null

fun getChatClient() : KotlinChatClient {
    if(client == null){
        client = KotlinChatClient()
    }
    return client!!
}