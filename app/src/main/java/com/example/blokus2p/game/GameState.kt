package com.example.blokus2p.game

import androidx.compose.ui.graphics.Color

data class GameState(
    val activPlayer_id: Int = 0,
    val isFinished: Boolean = false,
    val activPlayer: Player = Player(),
    val playerTwo: Player = Player(),
    val playerOneColor: Color = Color.Black,
    val playerTwoColor: Color = Color.Black,
    val board: GameBoard = BlokusBoard(),
    val boardGrid :Array<Int> = Array(14 * 14) { 0 },
    val selectedPolyomino: Polyomino = Polyomino()
)
