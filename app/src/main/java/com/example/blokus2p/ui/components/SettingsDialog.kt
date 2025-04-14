package com.example.blokus2p.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.blokus2p.model.Events.GameEvent
import com.example.blokus2p.game.GameState

@Composable
fun SettingsDialog(
    onDismissRequest: () -> Unit,
    onEvent: (GameEvent) -> Unit,
    gameState: GameState
) {
    // Temporäre State-Variablen, um Änderungen zu speichern
    var playerOneName by remember { mutableStateOf(gameState.activPlayer.name) }
    var playerTwoName by remember { mutableStateOf(gameState.playerTwo.name) }
    var playerOneColor by remember { mutableStateOf(gameState.activPlayer.color) }
    var playerTwoColor by remember { mutableStateOf(gameState.playerTwo.color) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(620.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "Einstellungen",
                    style = MaterialTheme.typography.bodyLarge
                )

                // Spieler 1 Einstellungen
                TextField(
                    value = playerOneName,
                    onValueChange = { playerOneName = it },
                    label = { Text("Spieler 1 Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                ColorPicker(
                    selectedColor = playerOneColor,
                    onColorChange = { playerOneColor = it },
                    label = "Spieler 1 Farbe"
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Spieler 2 Einstellungen
                TextField(
                    value = playerTwoName,
                    onValueChange = { playerTwoName = it },
                    label = { Text("Spieler 2 Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                ColorPicker(
                    selectedColor = playerTwoColor,
                    onColorChange = { playerTwoColor = it },
                    label = "Spieler 2 Farbe"
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            onEvent(GameEvent.GameRestart(playerOneName, playerTwoName, playerOneColor, playerTwoColor))
                            onDismissRequest()
                        },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = "Reset")
                    }
                    TextButton(
                        onClick = {
                            onEvent(GameEvent.ChangePlayerSettings(playerOneName, playerTwoName, playerOneColor, playerTwoColor))
                            onDismissRequest()
                        },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = "Bestätigen")
                    }
                }
            }
        }
    }
}

@Composable
fun ColorPicker(
    selectedColor: Color,
    onColorChange: (Color) -> Unit,
    label: String
) {
    val availableColors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan, Color.Magenta, Color.Gray)

    Column(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodySmall)

        Row(
            modifier = Modifier.padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color, shape = RoundedCornerShape(4.dp))
                        .border(
                            width = 2.dp,
                            color = if (color == selectedColor) Color.Black else Color.Transparent,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable { onColorChange(color) }
                )
            }
        }
    }
}

