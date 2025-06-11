package com.example.blokus2p.game

import androidx.compose.ui.graphics.Color

data class GameState(
    val players: List<Player> = listOf(),
    val activPlayer_id: Int = 0,
    val isFinished: Boolean = false,
    val activPlayer: Player = Player(),
    val playerOneColor: Color = Color.Black,
    val playerTwoColor: Color = Color.Black,
    var board: GameBoard = BlokusBoard(),
    val selectedPolyomino: Polyomino = Polyomino()
){
    fun getResult(perspectivePlayerId: Int): Double {
        val result =
             when {
                players[0].points > players[1].points -> 1.0 // Spieler 1 gewinnt
                players[0].points < players[1].points -> -1.0 // Spieler 2 gewinnt
                else -> 0.0 // Unentschieden
            }
        if(players[0].id == perspectivePlayerId)
            return result
        else
            return -result
    }
}
