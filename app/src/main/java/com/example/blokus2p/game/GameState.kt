package com.example.blokus2p.game

import androidx.compose.ui.graphics.Color

data class GameState(
    val players: List<Player> = listOf(),
    val activPlayer_id: Int = 0,
    val isFinished: Boolean = false,
    val activPlayer: Player = Player(),
    val playerOneColor: Color = Color.Black,
    val playerTwoColor: Color = Color.Black,
    var board: BlokusBoard = BlokusBoard(),
    val selectedPolyomino: Polyomino = Polyomino()
)
