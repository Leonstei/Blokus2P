package com.example.blokus2p.model.Events

sealed interface GameEvent :AppEvent {
    data class GameStarted(val playerCount: Int) : GameEvent
    data class GameEnded(val winner: Int) : GameEvent
    data class NextPlayer(val player: Int) : GameEvent
    data class PlacePolyomino(val col: Int, val row: Int) : GameEvent
    object UndoPlace: GameEvent
}