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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.example.blokus2p.ai.MinmaxAi
import com.example.blokus2p.ai.MonteCarloTreeSearchAi
import com.example.blokus2p.ai.RandomAi
import com.example.blokus2p.events.GameEvent
import com.example.blokus2p.game.GameState
import com.example.blokus2p.game.Player
import com.example.blokus2p.model.PlayerType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    onDismissRequest: () -> Unit,
    onEvent: (GameEvent) -> Unit,
    gameState: GameState
) {
    val playerOne = gameState.activPlayer
    val playerTwo = gameState.players.first { !it.isActiv } // Assuming there are exactly two players

    var playerOneName by remember { mutableStateOf(playerOne.name) }
    var playerTwoName by remember { mutableStateOf(playerTwo.name) }
    var playerOneColor by remember { mutableStateOf(playerOne.color) }
    var playerTwoColor by remember { mutableStateOf(playerTwo.color) }

    // State for selected player types
    var playerOneType by remember { mutableStateOf(getPlayerType(playerOne)) } // You might need to store player type in your Player class
    var playerTwoType by remember { mutableStateOf(getPlayerType(playerTwo)) } // You might need to store player type in your Player class

    // State for dropdown menu expansion
    var expandedPlayerOne by remember { mutableStateOf(false) }
    var expandedPlayerTwo by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(680.dp) // Increased height to accommodate dropdowns
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
                Spacer(modifier = Modifier.height(16.dp))

                // Spieler 1 Einstellungen
                Text("Spieler 1", style = MaterialTheme.typography.bodyMedium)
                TextField(
                    value = playerOneName,
                    onValueChange = { playerOneName = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Spieler 1 Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedPlayerOne,
                    onExpandedChange = { expandedPlayerOne = !expandedPlayerOne }
                ) {
                    TextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        readOnly = true,
                        value = playerOneType.name,
                        onValueChange = {},
                        label = { Text("Typ") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPlayerOne) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedPlayerOne,
                        onDismissRequest = { expandedPlayerOne = false }
                    ) {
                        PlayerType.entries.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption.name) },
                                onClick = {
                                    playerOneType = selectionOption
                                    expandedPlayerOne = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                ColorPicker(
                    selectedColor = playerOneColor,
                    onColorChange = { playerOneColor = it },
                    label = "Farbe"
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Spieler 2 Einstellungen
                Text("Spieler 2", style = MaterialTheme.typography.bodyMedium)
                TextField(
                    value = playerTwoName,
                    onValueChange = { playerTwoName = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Spieler 2 Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedPlayerTwo,
                    onExpandedChange = { expandedPlayerTwo = !expandedPlayerTwo }
                ) {
                    TextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        readOnly = true,
                        value = playerTwoType.name,
                        onValueChange = {},
                        label = { Text("Typ") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPlayerTwo) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedPlayerTwo,
                        onDismissRequest = { expandedPlayerTwo = false }
                    ) {
                        PlayerType.entries.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption.name) },
                                onClick = {
                                    playerTwoType = selectionOption
                                    expandedPlayerTwo = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                ColorPicker(
                    selectedColor = playerTwoColor,
                    onColorChange = { playerTwoColor = it },
                    label = "Farbe"
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            // You need to update your GameRestart event or create a new one
                            // to include the selected player types.
                            onEvent(GameEvent.GameRestart(playerOneName, playerTwoName, playerOneColor, playerTwoColor,playerOneType, playerTwoType))
                            onDismissRequest()
                        },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = "Reset")
                    }
                    TextButton(
                        onClick = {
                            // You need to update your ChangePlayerSettings event or create a new one
                            // to include the selected player types.
                            onEvent(GameEvent.ChangePlayerSettings(playerOneName, playerTwoName, playerOneColor, playerTwoColor, playerOneType, playerTwoType))
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
fun getPlayerType(player: Player): PlayerType {
    return if (player.isAi) {
        when (player.ai) {
            is RandomAi -> PlayerType.RandomAI
            is MinmaxAi -> PlayerType.MinimaxAI
            is MonteCarloTreeSearchAi -> PlayerType.MonteCarloAI
            else -> PlayerType.Human
        }
    } else {
        PlayerType.Human
    }
}

