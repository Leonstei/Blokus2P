package com.example.blokus2p.game

import com.example.blokus2p.model.SmalBoard
import com.example.blokus2p.model.SmalPlayer

interface GameRules {
    fun isValidPlacement(player: Player, polyominoCells: List<Int>, board: GameBoard): Boolean
    fun isValidPlacementSmal(
        player: SmalPlayer,
        polyominoCells: List<Int>,
        board: SmalBoard
    ): Boolean
}