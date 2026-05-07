package com.lucaslpmoura.android_chat.common

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun showErrorSnackbar(
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    viewModel: AndroidChatViewModel
) {
    scope.launch {
        val result = snackbarHostState.showSnackbar(
            message = viewModel.errorSnackBarText

        )

        when (result) {
            SnackbarResult.ActionPerformed, SnackbarResult.Dismissed
                -> viewModel.showErrorSnackbar = false
        }
    }

}