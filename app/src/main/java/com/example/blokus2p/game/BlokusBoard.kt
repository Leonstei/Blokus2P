package com.example.blokus2p.game

data class BlokusBoard(
    override val boardSize : Int = 14,
    override val boardGrid : Array<Int> = Array(boardSize * boardSize) { 0 },
    override val placedPolyominos: List<PlacedPolyomino> = listOf(),
 ) : GameBoard{
    override fun copyWith(
        boardGrid: Array<Int>,
        placedPolyominos: List<PlacedPolyomino>
    ): GameBoard = this.copy(
        boardGrid = boardGrid,
        placedPolyominos = placedPolyominos
    )
 }