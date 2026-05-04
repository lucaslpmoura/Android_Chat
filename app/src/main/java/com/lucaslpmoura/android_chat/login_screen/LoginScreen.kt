package com.lucaslpmoura.android_chat.login_screen

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lucaslpmoura.android_chat.common.AirPlaneModeListener
import com.lucaslpmoura.android_chat.common.AirPlaneModeReceiver
import com.lucaslpmoura.android_chat.ui.theme.Android_ChatTheme


class LoginScreen : ComponentActivity(), AirPlaneModeListener {

    private val viewModel by viewModels<LoginScreenViewModel>()


    private val airPlaneModeReceiver = AirPlaneModeReceiver(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(
            airPlaneModeReceiver,
            IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        )
        updateUIOnAirPlaneModeChange(airPlaneModeReceiver.checkAirPlaneMode(this))
        setContent {
            LoginScreenModel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    @Composable
    public fun LoginScreenModel() {
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        Scaffold(
            snackbarHost = {SnackbarHost(snackbarHostState)}
        ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("Android Chat")
                    Row() {
                        Text("Endereço do Servidor: ${viewModel.serverAddress}")
                        Button(
                            onClick = {viewModel.serverDialogValue = true}
                        ){
                            Text("Alterar...")
                        }
                    }
                    LoginComposable()
                    Row(
                    ) {
                        Button(
                            onClick = {shareOnWhatsapp()}
                        ) {
                            Text("Compartilhe no Whatsapp")
                        }
                        Button(
                            onClick = {sendBugFixEmail()}
                        ){
                            Text("Enviar relatório de bug")
                        }
                    }
                }

            ServerAddressDialog()
            }


    }

    private fun shareOnWhatsapp() {
        Intent(Intent.ACTION_SEND).also {
            it.setType("text/plain")
            it.setPackage("com.whatsapp")
            it.putExtra(Intent.EXTRA_TEXT, "Venha usar o android chat!")
            try{
                startActivity(Intent.createChooser(it, "Compratilhar com:"))
            }catch (e : ActivityNotFoundException) {
                e.printStackTrace()
            }

        }
    }

    private fun sendBugFixEmail (){
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Only email apps handle this.
            putExtra(Intent.EXTRA_EMAIL, arrayOf("lucas.moura@setis.com.br"))
            putExtra(Intent.EXTRA_SUBJECT, "[ANDROID_CHAT][BUG]")
            putExtra(Intent.EXTRA_TEXT, "Descreva seu bug aqui.")
        }
        try{
            startActivity(Intent.createChooser(intent, "Enviar relatório de bug"))
        }catch (e: ActivityNotFoundException){
            println("Could not resolve the activity.")
            e.printStackTrace()
        }
    }

    override fun updateUIOnAirPlaneModeChange(isAirPlaneModeOn : Boolean) {
        viewModel.isAirPlaneModeOn = isAirPlaneModeOn
    }

    @Composable
    fun LoginComposable(){
        if(!viewModel.isAirPlaneModeOn){
            return Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    state = rememberTextFieldState(),
                    label = {Text("Username")}
                )
                Button (
                    onClick = {}
                ) {
                    Text("Join")
                }
            }
        } else{
            return Text("Please turn off AirPlaneMode.")
        }
    }

    @Composable
    private fun ServerAddressDialog(){
        if(viewModel.serverDialogValue){
            Dialog(
                onDismissRequest = {viewModel.serverDialogValue = false},
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
                ){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextField(
                            state = viewModel.serverAddressTextFieldState,
                            label = {Text("Endereço")},
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
}