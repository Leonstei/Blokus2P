package com.example.blokus2p.model.Events

import androidx.compose.ui.graphics.Color

sealed interface GameEvent :AppEvent {
    data class GameStarted(val playerCount: Int) : GameEvent
    data class GameEnded(val winner: Int) : GameEvent
    data class GameRestart(val nameActivPlayer: String, val namePlayerTwo: String,
                           val  colorActivPlayer: Color, val colorPlayerTwo: Color) : GameEvent
    data class NextPlayer(val player: Int) : GameEvent
    data class PlacePolyomino(val col: Int, val row: Int) : GameEvent
    data class ChangePlayerSettings(
        val nameActivPlayer: String, val namePlayerTwo: String,
        val  colorActivPlayer: Color, val colorPlayerTwo: Color
    ) : GameEvent
    object UndoPlace: GameEvent
}