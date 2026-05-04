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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        return Android_ChatTheme {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("Android Chat")
                    Row() {
                        Text("Server Address: ${viewModel.serverAddress}")
                        Button(
                            onClick = {viewModel.serverDialogValue = true}
                        ){
                            Text("Change...")
                        }
                    }
                    viewModel.LoginComposable()
                    Row(
                    ) {
                        Button(
                            onClick = {shareOnWhatsapp()}
                        ) {
                            Text("Share on Whatsapp")
                        }
                        Button(
                            onClick = {sendBugFixEmail()}
                        ){
                            Text("Send a bug report")
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
    private fun ServerAddressDialog(){
        if(viewModel.serverDialogValue){
            Dialog(
                onDismissRequest = {viewModel.serverDialogValue = false},
                properties = DialogProperties(
                    dismissOnBackPress = true
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TextField(
                        state = rememberTextFieldState(),
                        label = {Text("Address")},

                    )
                }
            }
        }

    }

}