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
data class BlokusBoard2(
    val boardSize : Int = 14,
    val boardGrid : LongArray = LongArray(7),
    val placedPolyominos: List<PlacedPolyomino2> = listOf(),
) {
    //    override fun copyWith(
//        boardGrid: Array<Int>,
//        placedPolyominos: List<PlacedPolyomino2>
//    ): GameBoard = this.copy(
//        boardGrid = boardGrid,
//        placedPolyominos = placedPolyominos
//    )

    fun copyWith(boardGrid: LongArray, placedPolyominos: List<PlacedPolyomino2>): BlokusBoard2 =
        this.copy(
            boardGrid = boardGrid,
            placedPolyominos = placedPolyominos
        )

}