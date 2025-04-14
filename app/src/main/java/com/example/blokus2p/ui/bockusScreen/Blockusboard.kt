package com.example.blokus2p.ui.bockusScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.blokus2p.R
import com.example.blokus2p.model.Events.GameEvent
import com.example.blokus2p.game.Polyomino
import com.example.blokus2p.model.Events.PolyominoEvent
import com.example.blokus2p.ui.components.SettingsDialog
import com.example.blokus2p.viewModel.AppViewModel
import com.example.blokus2p.game.GameState

@Composable
fun BlockusScreen(viewModel: AppViewModel = viewModel()) {
    val gameState by viewModel.timerState.collectAsStateWithLifecycle()
    val cellSize = 28.dp
    val onEvent = viewModel::onEvent
    var showDialog by remember {
        mutableStateOf(false)
    }
    var isZooming by remember { mutableStateOf(false) }

    Column() {
        PlayerBar(gameState)
        Row {
            if (showDialog) {
                SettingsDialog(
                    onDismissRequest = { showDialog = false },
                    onEvent = onEvent,
                    gameState = gameState
                )
            }
            Button (onClick = {onEvent(GameEvent.NextPlayer(gameState.activPlayer_id))}) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.person), contentDescription = "")
                Icon(imageVector = ImageVector.vectorResource(R.drawable.arrow_forward), contentDescription = "RotateLeft")
            }
            Button (onClick = {onEvent(GameEvent.UndoPlace)}) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.undo), contentDescription = "undo")
            }
            IconButton(onClick = {showDialog = true}) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.more_vert), contentDescription = "redo")
            }
//            IconButton(onClick = {isZooming = isZooming.not()}) {
//                Icon(imageVector = ImageVector.vectorResource(R.drawable.more_vert), contentDescription = "redo")
//            }
        }
//        if(isZooming) {
//            TransformableBoard(cellSize = cellSize, gameState = gameState, onEvent = onEvent)
//        }else{
            BlockusBoard(gameState = gameState, cellSize = cellSize, onEvent = onEvent)
//        }
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
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
        .background(gameState.activPlayer.color))
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(20.dp)
        .background(gameState.activPlayer.color),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom) {
        Text("Spieler: ${gameState.activPlayer.name}")
        Text("Punkte: ${gameState.activPlayer.points}")
    }
}
@Composable
private fun TransformableBoard(
    cellSize: Dp = 28.dp,
    gameState: GameState,
    gridSize: Int = gameState.board.boardSize,
    onEvent: (GameEvent) -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 3f)
        offset += offsetChange
    }

    Box(
        Modifier
            // apply other transformations like rotation and zoom
            // on the pizza slice emoji
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
            // add transformable to listen to multitouch transformation events
            // after offset
            .transformable(state = state)
            .background(Color.Blue)
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in 0 until gridSize) {
                Row {
                    for (col in 0 until gridSize) {
                        val index = row * gridSize + col
                        Box(
                            modifier = Modifier
                                .size(cellSize)
                                .border(
                                    BorderStroke(
                                        2.dp, Brush.linearGradient(
                                            listOf(Color.Gray, Color.White)
                                        )
                                    )
                                )
                                .background(
                                    when (gameState.boardGrid[index]) {
                                        0 -> Color.LightGray
                                        1 -> gameState.playerOneColor
                                        else -> gameState.playerTwoColor
                                    }
                                )

//                                .pointerInput(Unit) {
//                                        detectTapGestures {
//                                            if (!isZooming && gameState.boardGrid[index] == 0) {
//                                                onEvent(GameEvent.PlacePolyomino(col, row))
//                                            }
//                                        }
//                                    }
                                .padding(1.dp)
                        ) {
                            if (index == 52 || index == 143) {
                                Text("X")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BlockusBoard(
    cellSize: Dp,
    onEvent: (GameEvent) -> Unit,
    gameState: GameState
) {
    val gridSize = gameState.board.boardSize
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var isZooming by remember { mutableStateOf(false) }

    // Zustand für die Transformationen
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 3f)
        offset += offsetChange
        isZooming = true  // Setzt isZooming auf true, wenn eine Zoom-Geste erkannt wird
    }

    Box(
        Modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
//            .pointerInput(Unit) {
//                // Nutze pointerInput für das Erkennen von Multi-Touch und Klicks
//                detectTransformGestures(
//                    onGesture = { _, pan, zoom, _ ->
//                        if (zoom != 1f) {
//                            // Wenn eine Zoom-Geste stattfindet, aktualisieren wir den Zoom-Faktor
//                            scale = (scale * zoom).coerceIn(1f, 3f)
//                            offset += pan
//                            isZooming = true
//                        } else {
//                            // Kein Zoom -> könnte ein Klick sein, Zoom wird beendet
//                            isZooming = false
//                        }
//                    }
//                )
//            }
            //.transformable(state = state)
            .background(Color.Blue)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in 0 until gridSize) {
                Row {
                    for (col in 0 until gridSize) {
                        val index = row * gridSize + col
                        Box(
                            modifier = Modifier
                                .size(cellSize)
                                .border(
                                    BorderStroke(
                                        2.dp, Brush.linearGradient(
                                            listOf(Color.Gray, Color.White)
                                        )
                                    )
                                )
                                .background(
                                    when (gameState.boardGrid[index]) {
                                        0 -> Color.LightGray
                                        1 -> gameState.playerOneColor
                                        else -> gameState.playerTwoColor
                                    }
                                )
//                                .pointerInput(isZooming) {
//                                    // Diese pointerInput blockiert Klicks während des Zoomens
//                                    detectTapGestures {
//                                        if (!isZooming && gameState.boardGrid[index] == 0) {
//                                            onEvent(GameEvent.PlacePolyomino(col, row))
//                                        }
//                                    }
//                                }
                                .clickable {
                                    if (gameState.boardGrid[index] == 0) {
                                        onEvent(GameEvent.PlacePolyomino(col, row))
                                    }
                                }
                                .padding(1.dp)
                        ) {
                            if (index == 52 || index == 143) {
                                Text("X")
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun Polyominos( cellSize: Dp, onEvent: (PolyominoEvent) -> Unit,gameState: GameState) {
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

    val minX = polyomino.currentVariant.minOf { it.first }
    val minY = polyomino.currentVariant.minOf { it.second }
    val maxX = polyomino.currentVariant.maxOf { it.first } +1
    val maxY = polyomino.currentVariant.maxOf { it.second } +1
    Box(
        modifier = Modifier
            .width(cellSize * maxX)
            .height(cellSize * maxY)
            .border(2.dp, borderColor)
    ) {
        polyomino.currentVariant.forEach { (x, y) ->
            Box(
                modifier = Modifier
                    .offset(
                        x = (x - minX) * cellSize,
                        y = (y - minY) * cellSize
                    )
                    .clickable {
                        if (player == gameState.activPlayer.id) onEvent(
                            PolyominoEvent.PolyominoSelected(
                                polyomino,
                                Pair(x, y)
                            )
                        )
                    }
                    .border(
                        1.dp, if (polyomino.selectedCell == Pair(x, y) &&
                            polyomino.name == gameState.selectedPolyomino.name &&
                            player == gameState.activPlayer_id
                        ) Color.Red else Color.Transparent
                    )
                    .size(cellSize)
                    .background(if (player == 1) gameState.playerOneColor else gameState.playerTwoColor)

            )
        }
    }

}

