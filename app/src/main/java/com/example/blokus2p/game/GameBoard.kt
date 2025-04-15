package com.example.blokus2p.game

interface GameBoard {
    val boardSize: Int
    val boardGrid: Array<Int>
    val placedPolyominos: List<PlacedPolyomino>
    fun copyWith(
        boardGrid: Array<Int> = this.boardGrid,
        placedPolyominos: List<PlacedPolyomino> = this.placedPolyominos
    ): GameBoard
}