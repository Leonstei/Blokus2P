package com.example.blokus2p.game

interface GameBoard {
    val boardSize: Int
    val boardGrid: LongArray
    val placedPolyominos: List<PlacedPolyomino>
    fun copyWith(
        boardGrid: LongArray = this.boardGrid,
        placedPolyominos: List<PlacedPolyomino> = this.placedPolyominos
    ): GameBoard
}