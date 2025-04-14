package com.example.blokus2p.game

interface GameRules {
    fun isValidPlacement(player: Player, polyomino: Polyomino, board: GameBoard,selectedPosition : Pair<Int,Int>): Boolean
}