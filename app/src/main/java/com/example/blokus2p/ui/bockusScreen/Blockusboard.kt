package com.example.blokus2p.ui.bockusScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.blokus2p.R
import com.example.blokus2p.model.Events.GameEvent
import com.example.blokus2p.model.Events.Polyomino
import com.example.blokus2p.model.Events.PolyominoEvent
import com.example.blokus2p.viewModel.AppViewModel
import com.example.blokus2p.viewModel.GameState

@Composable
fun BlockusScreen(viewModel: AppViewModel = viewModel()) {
    val gameState by viewModel.timerState.collectAsStateWithLifecycle()
    val polyominoSate by viewModel.polyominoState.collectAsStateWithLifecycle()
    var cellSize = 28.dp
    val onEvent = viewModel::onEvent

    Column() {
        PlayerBar(gameState)
        Row {
            Button (onClick = {onEvent(GameEvent.NextPlayer(gameState.activPlayer_id))}) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.person), contentDescription = "")
                Icon(imageVector = ImageVector.vectorResource(R.drawable.arrow_forward), contentDescription = "RotateLeft")
            }
            Button (onClick = {onEvent(GameEvent.UndoPlace)}) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.undo), contentDescription = "undo")
            }
            IconButton(onClick = {}) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.more_vert), contentDescription = "redo")
            }
        }
        BlockusBoard(cellSize,onEvent,gameState)
        Row {
            IconButton (onClick = {onEvent(PolyominoEvent.PolyominoRotate)}) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.rotat360), contentDescription = "")
            }
            IconButton(onClick = {onEvent(PolyominoEvent.PolyominoRotateClockwise)}) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.rotate_right), contentDescription = "")
            }
            IconButton(onClick = {onEvent(PolyominoEvent.PolyominoRotateCounterClockwise)}) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.rotate_left), contentDescription = "RotateLeft")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Polyominos( cellSize, onEvent,gameState)


    }
}
@Composable
fun PlayerBar(gameState: GameState){
    Spacer(modifier = Modifier.fillMaxWidth()
        .height(40.dp)
        .background(gameState.activPlayer.color))
    Row(modifier = Modifier.fillMaxWidth().height(20.dp)
        .background(gameState.activPlayer.color),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom) {
        Text("Spieler: ${gameState.activPlayer.name}")
        Text("Punkte: ${gameState.activPlayer.points}")
    }
}

@Composable
fun BlockusBoard(cellSize: Dp,onEvent: (GameEvent) -> Unit,gameState: GameState) {
    val gridSize = 14
    // Zustand für die Farben des Spielfelds (jede Zelle wird mit einer Farbe belegt)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gitterlayout
        for (row in 0 until gameState.gridSize) {
            Row {
                for (col in 0 until gameState.gridSize) {
                    val index = row * gridSize + col
                    Box(
                        modifier = Modifier
                            .size(cellSize)
                            .border(
                                BorderStroke(2.dp, Brush.linearGradient(
                                    listOf(Color.Gray, Color.White)
                                ))
                            )
                            .background(
                                if(gameState.boardGrid[index] == 0) Color.LightGray
                                else if(gameState.boardGrid[index] == 1) gameState.playerOneColor else gameState.playerTwoColor
                            )
                            .clickable {
                                if(gameState.boardGrid[index] == 0){
                                    onEvent(GameEvent.PlacePolyomino(col,row))
                                }
                            }
                            .padding(1.dp),

                    ){
                        if(index == 52 || index == 143){
                            Text("X")
                        }else{
                            Text("$index")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Polyominos( cellSize: Dp, onEvent: (PolyominoEvent) -> Unit,gameState: GameState) {
    Spacer(modifier = Modifier.height(12.dp))
    if (gameState.activPlayer_id == gameState.activPlayer.id) {
        PolyominoRow(gameState.activPlayer.polyominos, cellSize, onEvent,gameState,gameState.activPlayer.id)
        Spacer(modifier = Modifier.height(12.dp))
        PolyominoRow(gameState.playerTwo.polyominos, cellSize, onEvent,gameState,gameState.playerTwo.id)
    } else {
        PolyominoRow(gameState.playerTwo.polyominos, cellSize, onEvent,gameState,gameState.playerTwo.id)
        Spacer(modifier = Modifier.height(12.dp))
        PolyominoRow(gameState.activPlayer.polyominos, cellSize, onEvent,gameState,gameState.activPlayer.id)
    }

}
@Composable
fun PolyominoRow(
    polyominos: List<Polyomino>,
    cellSize: Dp,
    onEvent: (PolyominoEvent) -> Unit,
    gameState: GameState,
    player: Int
){
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(polyominos.size) { index ->
            val polyomino = polyominos[index]
            Polyomino(polyomino, cellSize, onEvent,gameState,player)
        }
    }
}

@Composable
fun Polyomino(
    polyomino: Polyomino,

    cellSize: Dp,
    onEvent: (PolyominoEvent) -> Unit,
    gameState: GameState,
    player: Int
){
    val borderColor = if (gameState.selectedPolyomino.name == polyomino.name && player == gameState.activPlayer_id) Color.Red else Color.Transparent

    val minX = polyomino.cells.minOf { it.first }
    val minY = polyomino.cells.minOf { it.second }
    val maxX = polyomino.cells.maxOf { it.first } +1
    val maxY = polyomino.cells.maxOf { it.second } +1
    Box(
        modifier = Modifier
            .width(cellSize * maxX)
            .height(cellSize * maxY)
            .border(2.dp, borderColor)
    ) {
        polyomino.cells.forEach { (x, y) ->
            Box(

                modifier = Modifier
                    .offset(
                        x = (x - minX) * cellSize,
                        y = (y - minY) * cellSize
                    ).clickable { if(player == gameState.activPlayer.id) onEvent(PolyominoEvent.PolyominoSelected(polyomino,Pair(x,y))) }
                    .border(1.dp, if(polyomino.selectedCell == Pair(x,y) &&
                        polyomino.name == gameState.selectedPolyomino.name &&
                        player == gameState.activPlayer_id) Color.Red else Color.Transparent)
                    .size(cellSize)
                    .background(if(player == 1) gameState.playerOneColor else gameState.playerTwoColor)

            )
        }
    }

}

