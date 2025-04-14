package com.example.blokus2p.game

class BlokusRules: GameRules {

    override fun isValidPlacement(
        player: Player,
        polyomino: Polyomino,
        board: GameBoard,
        selectedPosition: Pair<Int, Int>
    ): Boolean {
        return true
    }
}