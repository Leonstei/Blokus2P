package com.example.blokus2p.game

interface GameBoard {
    val boardSize: Int
    val boardGrid: Array<Int>
    var placedPolyominos: MutableMap<Int,List<Pair<Int,Int>>>
    fun putPlacedPolyomino(playerId: Int, cells: List<Pair<Int,Int>>): GameBoard
}