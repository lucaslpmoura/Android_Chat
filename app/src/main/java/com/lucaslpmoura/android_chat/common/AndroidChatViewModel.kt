package com.lucaslpmoura.android_chat.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lucaslpmoura.kotlin_chat.client.KotlinChatClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class AndroidChatViewModel(private val client : KotlinChatClient) : ViewModel(){



    var showErrorSnackbar by mutableStateOf(false)
    var errorSnackBarText by mutableStateOf("")

    val clientScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

}