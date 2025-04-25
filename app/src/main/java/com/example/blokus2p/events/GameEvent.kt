package com.example.blokus2p.events

import android.icu.text.PluralRules.PluralType
import androidx.compose.ui.graphics.Color
import com.example.blokus2p.model.PlayerType

sealed interface GameEvent : AppEvent {
    data class GameStarted(val playerCount: Int) : GameEvent
    data class GameEnded(val winner: Int) : GameEvent
    data class GameRestart(val nameActivPlayer: String, val namePlayerTwo: String,
                           val  colorActivPlayer: Color, val colorPlayerTwo: Color,
                           val playerOneType: PlayerType, val playerTwoType: PlayerType
        ) : GameEvent
    data class NextPlayer(val player: Int) : GameEvent
    data class PlacePolyomino(val col: Int, val row: Int) : GameEvent
    data class ChangePlayerSettings(
        val nameActivPlayer: String, val namePlayerTwo: String,
        val  colorActivPlayer: Color, val colorPlayerTwo: Color,
        val playerOneType: PlayerType, val playerTwoType: PlayerType
    ) : GameEvent
    object UndoPlace: GameEvent
}