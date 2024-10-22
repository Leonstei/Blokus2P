package com.example.blokus2p.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.blokus2p.model.Events.GameEvent

@Composable
fun SettingsDialog(
    onDismissRequest: () -> Unit,
    onEvent: (GameEvent) -> Unit,
    gameEvent: GameEvent
){


    Dialog(onDismissRequest = {onDismissRequest()}) {
        Card(modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .height(420.dp)
        )
        {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Spacer(modifier = Modifier.height(32.dp))

                Row (horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){

                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){

                }
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.Bottom
                ){
                    TextButton(
                        onClick = {

                        },
                        modifier = Modifier
                            .padding(top = 8.dp, start = 120.dp)

                    ) {
                        Text(text = "Bestätigen")
                    }
                }

            }
        }
    }
}