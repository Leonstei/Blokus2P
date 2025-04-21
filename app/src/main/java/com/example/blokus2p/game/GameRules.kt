package com.example.blokus2p.game

interface GameRules {
    fun isValidPlacement(player: Player, polyominoCells: List<Pair<Int,Int>>, board: GameBoard,selectedPosition : Pair<Int,Int>): Boolean
}