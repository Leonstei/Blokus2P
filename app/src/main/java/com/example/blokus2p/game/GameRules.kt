package com.example.blokus2p.game

interface GameRules {
    fun isValidPlacement(player: Player, polyominoCells: List<Int>, board: BlokusBoard, selectedPosition : Int): Boolean
}