package com.lucaslpmoura.android_chat.login_screen

import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
public fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    navigateToRoomsList : () -> Unit
) {
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                    viewModel.updateUIOnAirPlaneModeChange(context!!)
                }
            }
        }


        context.registerReceiver(
            receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        )

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    viewModel.updateUIOnAirPlaneModeChange(context)


    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color.Red
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("Android Chat")

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {


                Text("Endereço do Servidor:")
                Text(viewModel.serverAddress)
                Button(
                    onClick = { viewModel.serverDialogValue = true }
                ) {
                    Text("Alterar...")
                }
            }


            LoginComposable(viewModel, navigateToRoomsList)
            Row(
            ) {
                Button(
                    onClick = { shareOnWhatsapp(context) }
                ) {
                    Text("Compartilhe no Whatsapp")
                }
                Button(
                    onClick = { sendBugFixEmail(context) }
                ) {
                    Text("Enviar relatório de bug")
                }
            }
        }

        ServerAddressDialog(viewModel)
        when (viewModel.showErrorSnackbar) {
            true -> showErrorSnackBar(snackbarHostState, scope, viewModel)
            false -> {}
        }

    }


}

private fun shareOnWhatsapp(context: Context) {
    Intent(Intent.ACTION_SEND).also {
        it.setType("text/plain")
        it.setPackage("com.whatsapp")
        it.putExtra(Intent.EXTRA_TEXT, "Venha usar o android chat!")
        try {
            context.startActivity(Intent.createChooser(it, "Compratilhar com:"))
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }

    }
}

private fun sendBugFixEmail(context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // Only email apps handle this.
        putExtra(Intent.EXTRA_EMAIL, arrayOf("lucas.moura@setis.com.br"))
        putExtra(Intent.EXTRA_SUBJECT, "[ANDROID_CHAT][BUG]")
        putExtra(Intent.EXTRA_TEXT, "Descreva seu bug aqui.")
    }
    try {
        context.startActivity(Intent.createChooser(intent, "Enviar relatório de bug"))
    } catch (e: ActivityNotFoundException) {
        println("Could not resolve the activity.")
        e.printStackTrace()
    }
}


@Composable
fun LoginComposable(
    viewModel: LoginViewModel,
    navigateToRoomsList: () -> Unit
) {
    when(viewModel.connectionState){
        LoginViewModel.ConnectionState.NOT_CONNECTED -> {
            if (!viewModel.isAirPlaneModeOn) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        state = viewModel.userNameTextState,
                        label = { Text("Nome de usuário") },

                    )
                    Button(
                        onClick = {
                            viewModel.connectToServer()
                        }
                    ) {
                        Text("Conectar")
                    }
                }
            } else {
                return Text("Por favor desabilite o modo avião.")
            }
        }

        LoginViewModel.ConnectionState.CONNECTING -> CircularProgressIndicator()

        LoginViewModel.ConnectionState.CONNECTED -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("You are connected as ${viewModel.name}.")
                Button(
                    onClick = { navigateToRoomsList() }
                ) {
                    Text("Ver Salas")
                }
            }

        }
    }



}

@Composable
private fun ServerAddressDialog(viewModel: LoginViewModel) {
    if (viewModel.serverDialogValue) {
        Dialog(
            onDismissRequest = { viewModel.serverDialogValue = false },
            properties = DialogProperties(
                dismissOnBackPress = true
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TextField(
                        state = viewModel.serverAddressTextFieldState,
                        label = { Text("Endereço") },
                    )
                    Button(
                        onClick = {
                            viewModel.changeServerAddress(viewModel.serverAddressTextFieldState.text.toString())
                            viewModel.serverDialogValue = false
                        }
                    ) {
                        Text("Alterar")
                    }
                }
            }

        }
    }
}


private fun showErrorSnackBar(
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    viewModel: LoginViewModel
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